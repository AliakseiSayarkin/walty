# Transaction Record Microservice

## Overview

This Transaction Record Microservice is implemented in Java using the Spring Boot framework.
It provides functionalities for retrieving, storing and modifying currency transactions,
allowing users to return transaction records / summary for specified amount of time.

## Endpoints

1. ### Get transaction for current month
   ### Request
   #### Endpoint:
        GET /v1/transaction/date/{date}/{telegramChatId}
   #### Parameters:
    * telegramChatId – telegram chat id for witch transaction records will be returned
    * date – date from witch transaction records will be returned, use current for beginning of a current month
   ### Response
    * Status Code: 200 OK
   #### Body:
        [
            {
               "id": "65d13447285ad11c92b703f1",
               "telegramChatId": "1",
               "date": "2024-02-17T22:33:43",
               "description": "description",
               "USD": 200.0,
               "EUR": 400.0,
               "BYN": 666.0,
               "RUB": 300.0
            },
            {
               "id": "65d13448285ad11c92b703f2",
               "telegramChatId": "1",
               "date": "2024-02-17T22:33:44",
               "description": "description",
               "USD": 200.0,
               "EUR": 400.0,
               "BYN": 100.0,
               "RUB": 300.0
            },
            {
               "id": "65d92e0dabcfd852b7c47424",
               "telegramChatId": "1",
               "date": "2024-02-23T23:45:17",
               "description": "description",
               "USD": 200.0,
               "EUR": 400.0,
               "BYN": 100.0,
               "RUB": 300.0
            },
            {
               "id": "65d92e13abcfd852b7c47425",
               "telegramChatId": "1",
               "date": "2024-02-23T23:45:23",
               "description": "description",
               "USD": 200.0,
               "EUR": 400.0,
               "BYN": 100.0,
               "RUB": 300.0
            },
            {
               "id": "65d92e13abcfd852b7c47426",
               "telegramChatId": "1",
               "date": "2024-02-23T23:45:23",
               "description": "description",
               "USD": 200.0,
               "EUR": 400.0,
               "BYN": 100.0,
               "RUB": 300.0
            }
        ]

2. ### Get transaction summary
   ### Request
   #### Endpoint:
        GET /v1/transaction/date/{date}/summary/{telegramChatId}
   #### Parameters:
    * telegramChatId – telegram chat id for witch transaction summary will be returned
    * date – date from witch transaction summary will be returned
   #### Response
    * Status Code: 200 OK
   #### Body:
        {
            "telegramChatId": "1",
            "USD": 1000.0,
            "EUR": 2000.0,
            "BYN": 1066.0,
            "RUB": 1500.0
        }
3. ### Delete transaction records by telegram chat id
   ### Request
   #### Endpoint:
        DELETE /v1/transaction/telegram/chat/id/{telegramChatId}
   #### Parameters:
    * telegramChatId – telegram chat id for witch transaction records will be deleted
   #### Response
    * Status Code: 200 OK
   
4. ### Delete transaction record by id
   ### Request
   #### Endpoint:
        DELETE /v1/transaction/id/{id}
   #### Parameters:
    * id – id for witch transaction records will be deleted
   #### Response
    * Status Code: 200 OK   

5. ### Save transaction record
   ### Request
   #### Endpoint:
        POST v1/transaction
   #### Body:
        {
            "telegramChatId": "2",
            "USD":"200",
            "EUR":"400",
            "BYN":"100",
            "RUB":"300",
            "description": "description"
        }
   #### Response
    * Status Code: 200 OK
   #### Body:
       {
           "id": "65de21466475af23a28bfcc7",
           "telegramChatId": "2",
           "date": "2024-02-27T17:52:06",
           "description": "description",
           "USD": 200.0,
           "EUR": 400.0,
           "BYN": 100.0,
           "RUB": 300.0
       }
6. ### Update transaction record
   ### Request
   #### Endpoint:
        PUT v1/transaction
   #### Body:
        {
            "id": "65d13447285ad11c92b703f1", <-- Required field
            "telegramChatId": "2", <-- optional fields, add only thouse you want to update them
            "BYN":"666",
            "USD":"200",
            "RUB":"300",
            "EUR":"400",
            "description": "description"
        }
   #### Response
    * Status Code: 200 OK
   #### Body:
        {
            "id": "65d13447285ad11c92b703f1",
            "telegramChatId": "2",
            "date": "2024-02-17T22:33:43",
            "description": "description",
            "USD": 200.0,
            "EUR": 400.0,
            "BYN": 666.0,
            "RUB": 300.0
        }

## Error Handling

In case of errors, the microservice returns HTTP status 404 Not Found.

## Dependencies

* Java 21
* Spring Boot 3.2.2
* Gradle 8.5
* Groovy 5.0