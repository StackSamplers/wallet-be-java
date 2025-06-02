```shell
./deploy.sh 8088 /opt/projects/env/wallet-dev.env
```







# API JSON Response Format

This document describes the standardized JSON response formats.

## Response Structure

### Success Response Format

```json
{
    "error": false,
    "status": "OK",
    "message": "Operation successfully completed",
    "data": {
        // Response payload
    },
    "time": "2025-06-02 15:52:33"
}
```

### Error Response Format

```json
{
    "error": true,
    "traceId": "683d9f681a57db4ef3e5aa046856dfb7",
    "path": "/api/v1/auth/login",
    "status": "UNAUTHORIZED",
    "businessErrorCode": 1004,
    "message": "You are not authorized to do this!",
    "time": "2025-06-02 15:56:08",
    "hasValidationErrors": false,
    "validationErrors": {
        // Field-specific validation errors (when applicable)
    }
}
```

## Field Descriptions

### Common Fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `error` | boolean | ✅ | Indicates response is an error |
| `status` | string | ✅ | HTTP status code as string (e.g., "OK", "BAD_REQUEST", "UNAUTHORIZED") |
| `message` | string | ✅ | translated error message (via Accept-Language header) |
| `time` | string | ✅ | Server timestamp when the response was generated (format: YYYY-MM-DD HH:mm:ss) |

### Success Response Fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `data` | object/array | ✅ | Contains response payload (entity, list, etc.) |

### Error Response Fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `traceId` | string | ✅ | Unique identifier for request tracing |
| `path` | string | ✅ | API endpoint path |
| `businessErrorCode` | number | ✅ | Custom app-specific error code |
| `hasValidationErrors` | boolean | ✅ | Indicates response has validation errors |
| `validationErrors` | object | ❌ | Key-value pairs of field names and their validation error messages |

## Response Examples

### 1. Single Entity Response

Used when returning a single resource (e.g., user creation, get user by ID).

```json
{
    "error": false,
    "status": "OK",
    "message": "Operation successfully completed",
    "data": {
        "createdDate": "2025-05-28T15:16:36.825636",
        "createdBy": "SYSTEM",
        "id": 1,
        "email": "johndoe@example.com",
        "name": "John",
        "surname": "John"
    },
    "time": "2025-06-02 15:52:33"
}
```

### 2. Paginated List Response

Used for search endpoints and lists with pagination support.

```json
{
    "error": false,
    "status": "OK",
    "message": "Data retrived successfully",
    "data": {
        "content": [
            {
                "createdDate": "2025-05-28T15:16:36.825636",
                "createdBy": "SYSTEM",
                "deleted": false,
                "id": 1,
                "email": "johndoe@example.com",
                "name": "John",
                "surname": "John"
            }
        ],
        "pageable": {
            "pageNumber": 0,
            "pageSize": 30,
            "sort": {
                "empty": false,
                "sorted": true,
                "unsorted": false
            },
            "offset": 0,
            "paged": true,
            "unpaged": false
        },
        "last": true,
        "totalElements": 1,
        "totalPages": 1,
        "size": 30,
        "number": 0,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "first": true,
        "numberOfElements": 1,
        "empty": false
    },
    "time": "2025-06-02 15:54:04"
}
```

### 3. Error

```json
{
    "error": true,
    "traceId": "683d9f681a57db4ef3e5aa046856dfb7",
    "path": "/api/v1/auth/login",
    "status": "UNAUTHORIZED",
    "businessErrorCode": 1004,
    "message": "You are not authorized to do this!",
    "time": "2025-06-02 15:56:08",
    "hasValidationErrors": false
}
```

### 4. Validation Error

```json
{
    "error": true,
    "traceId": "683d9f3b3eb468b32952f9eb5304637c",
    "path": "/api/v1/users",
    "status": "BAD_REQUEST",
    "businessErrorCode": 1000,
    "message": "Validation failed!",
    "time": "2025-06-02 15:55:23",
    "hasValidationErrors": true,
    "validationErrors": {
        "password": "length must be between 5 and 25",
        "name": "length must be between 2 and 50"
    }
}
```
