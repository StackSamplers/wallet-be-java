```shell

nohup java -jar target/project.jar > app.log 2>&1 &

nohup java -jar target/project.jar --server.port=8080 > app.log 2>&1 &

nohup java -jar target/project.jar --server.port=8080 --other.property=value > app.log 2>&1 &

```