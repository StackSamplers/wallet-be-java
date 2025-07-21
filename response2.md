```json

{
    "error": true,
    "traceId": "683d9f681a57db4ef3e5aa046856dfb7",
    "businessErrorCode": 1004,
    "message": "You have validation errors!",
    "validationErrors": {
        "field1": "error1",
	"field2": "error2"
    }
}
--------------------------------------------------------
{
    "error": false,
    "traceId": "683d9f681a57db4ef3e5aa046856dfb7",
    "message": "Operation successfully completed",
    "data": {
        "id": 1,
        "email": "johndoe@example.com"
    }
}

{
    "error": false,
    "traceId": "683d9f681a57db4ef3e5aa046856dfb7",
    "message": "Data retrieved successfully",
    "data": {
        "content": [
            {
                "id": 1,
                "email": "johndoe@example.com"
            }
        ],
        "pageable": {
            "totalElements": 1,
            "numberOfElements": 1,
            "totalPages": 1,
            "size": 30,
            "last": true,
            "first": true,
            "empty": false
        }
    }
}


```
