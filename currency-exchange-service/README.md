# Currency Exchange Microservice

## Overview

This Currency Exchange Microservice is implemented in Java using the Spring Boot framework. 
It provides functionalities for currency exchange, allowing users to get real-time exchange rates
and perform currency conversions.

## Endpoints

1. ### Convert currency
   ### Request
   #### Endpoint:
        GET /v1/currency/exchange/{currencyToBuyCode}
   #### Parameters:
    * currencyToBuyCode – currency you want to buy (convert to)
   #### Body:
        {
            "code" : "BYN", <- currency to sell code
            "value" : 100   <- ammount of currency to sell
        }
   ### Response
    * Status Code: 200 OK
   #### Body:
         {
            "code": "USD",    <- bought currency code
            "value": 30.0515  <- ammount of bought currency
         }

2. ### Get currency exchange values for a given currency
   ### Request
   #### Endpoint:
        GET /v1/currency/values
   #### Body:
        {
            "code" : "BYN", <- currency code
            "value" : 100   <- ammount of currency to sell
        }
   ### Response
   * Status Code: 200 OK
   #### Body:
        {
            "USD": 30.7399,
            "EUR": 28.5446,
            "BYN": 100.0, <- same value as in request
            "RUB": 2808.4119
        }
## Error Handling

In case of errors, the microservice returns HTTP status 404 Not Found

## Dependencies

* Java 21
* Spring Boot 3.2.2
* Gradle 8.5
* Groovy 5.0

