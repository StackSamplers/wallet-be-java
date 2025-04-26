#!/bin/bash
#./deploy.sh 8085 /opt/projects/env/project-dev.env
# Improved deployment script with graceful shutdown, custom .env file support, and management port config
echo "Starting deployment process..."

# Set default port or use the value passed as first argument
PORT=${1:-8085}
# Calculate management port (server port + 1)
MANAGEMENT_PORT=$((PORT + 1))

# Set default .env file path or use the value passed as second argument
ENV_FILE=${2:-".env"}

echo "Using server port: $PORT"
echo "Using management port: $MANAGEMENT_PORT"
echo "Using env file: $ENV_FILE"

# Pull latest changes from git repository
echo "Pulling latest changes from git..."
git pull

# Find and gracefully shut down the existing Spring Boot application
echo "Looking for existing Spring Boot application on port $PORT..."
PID=$(lsof -t -i:$PORT)
if [ -n "$PID" ]; then
    echo "Found application running with PID: $PID"
    echo "Sending graceful shutdown signal..."
    kill -15 $PID  # SIGTERM for graceful shutdown

    # Wait for the application to shut down (with timeout)
    echo "Waiting for application to shut down gracefully..."
    TIMEOUT=30
    while [ $TIMEOUT -gt 0 ] && kill -0 $PID 2>/dev/null; do
        sleep 1
        ((TIMEOUT--))
        echo -n "."
    done
    echo ""

    # If process is still running after timeout, force kill
    if kill -0 $PID 2>/dev/null; then
        echo "Application did not shut down gracefully within timeout. Force killing..."
        kill -9 $PID
    else
        echo "Application shut down gracefully."
    fi
else
    echo "No application found running on port $PORT."
fi

# Build the project with package by default
echo "Building project with Maven..."
mvn clean package -DskipTests=true

# Create timestamped log file
LOG_FILE="app_$(date '+%Y-%m-%d_%H-%M-%S').log"
echo "Created log file: $LOG_FILE"

# Find the first JAR file in the target directory
JAR_FILE=$(find target -name "*.jar" -type f | head -n 1)

if [ -z "$JAR_FILE" ]; then
    echo "Error: No JAR file found in target directory!"
    exit 1
fi

echo "Found JAR file: $JAR_FILE"

# Load environment variables
ENV_PARAMS=""

if [ -f "$ENV_FILE" ]; then
    echo "Found env file at $ENV_FILE. Reading environment variables..."
    # Read .env file and convert to Java properties format
    while IFS='=' read -r key value || [ -n "$key" ]; do
        # Skip comments and empty lines
        [[ $key == \#* ]] && continue
        [[ -z "$key" ]] && continue

        # Trim whitespace from key and value
        key=$(echo "$key" | xargs)
        value=$(echo "$value" | xargs)

        # Add environment variable to Java parameters
        ENV_PARAMS="$ENV_PARAMS -D$key=$value"
    done < "$ENV_FILE"

    echo "Environment variables loaded successfully"
else
    echo "Warning: Env file $ENV_FILE not found. Continuing without environment variables."
fi

# Run the Spring Boot application in the background with the specified port and env variables
echo "Starting Spring Boot application on port $PORT with management port $MANAGEMENT_PORT..."
nohup java $ENV_PARAMS -jar "$JAR_FILE" \
  --server.port=$PORT \
  --management.server.port=$MANAGEMENT_PORT > "$LOG_FILE" 2>&1 &

echo "Deployment completed! Application is running in the background."
echo "Server port: $PORT"
echo "Management/Actuator port: $MANAGEMENT_PORT"
echo "Logs are being written to: $LOG_FILE"
echo "Use 'tail -f $LOG_FILE' to view logs"