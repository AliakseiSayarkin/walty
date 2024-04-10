# Walty - Tracker Telegram Bot - Microservices Architecture

## Introduction

This project implements a microservices architecture using Spring Framework to create an expense tracking Telegram bot.
Each microservice is responsible for a specific functionality, promoting modularity, scalability, and maintainability.

## Technology Stack

* Programming Language: Java
* Spring Framework: Core framework for building microservices
* Spring Boot: Simplifies microservice development by providing autoconfiguration and starter dependencies
* Spring Cloud: Offers tools for distributed system development, including service discovery and configuration management
* Database (MongoDB): For persistent storage of transaction records (implementation details vary)
* Telegram Bot API: Interface for interacting with the Telegram bot platform

## Microservices

* currency-exchange-service: Provides real-time currency exchange rates for expense tracking in different currencies.
* discovery-service: Facilitates service discovery, allowing microservices to locate each other dynamically.
* transaction-record-service_db: Manages persistent storage of transaction records in a database.
* transaction-record-service: Handles business logic for recording and retrieving transaction data, potentially interacting with transaction-record-service_db.
* telegram-bot-service: Interacts with the Telegram API to receive user commands, process expense tracking requests, and send responses through the Telegram bot.
* config-service: Provides centralized configuration management for all microservices, reducing redundancy and simplifying environment-specific adjustments.

## Port Mapping

| Service                       | Port |
|-------------------------------|------|
| currency-exchange-service     | 8101 |
| discovery-service             | 8102 |
| transaction-record-service_db | 8103 |
| transaction-record-service    | 8104 |
| telegram-bot-service          | 8105 |
| config-service                | 8106 |

## Microservice Description

Each microservice has its own README.md

* currency-exchange-service - https://github.com/AliakseiSayarkin/walty/tree/master/currency-exchange-service#readme
* discovery-service - https://github.com/AliakseiSayarkin/walty/tree/master/discovery-service#readme
* transaction-record-service - https://github.com/AliakseiSayarkin/walty/tree/master/transaction-record-service#readme
* telegram-bot-service - https://github.com/AliakseiSayarkin/walty/tree/master/telegram-bot-service#readme
* config-service - https://github.com/AliakseiSayarkin/walty/tree/master/config-service#readme

## Basic Commands For Telegram Bot

* ### Get Transaction Records

  #### Description:

  Gets all transaction records for a given user

  #### Command

        /getTransactionRecords

  #### Response

        ----------------
        id: 6612b6c999567144919d1699
        date: 2024-04-07 18:07:53
        description: Expense
        byn: 9.8026
        ----------------
        id: 6612b6be99567144919d1697
        date: 2024-04-07 18:07:42
        description: Expense
        byn: 3.2675
        ----------------

* ### Get Currency Exchange Rates

  #### Description:

  Gets real-time exchange rates for a currency with given value (value = 1 by default)

  #### Command

        /exchange {id} {value} (value is optional)

  #### Response

        USD = 1.0
        EUR = 0.9265
        BYN = 3.2654
        RUB = 92.496

For full list of commands please go to: https://github.com/AliakseiSayarkin/walty/tree/master/telegram-bot-service#readme

## Prerequisites

* Java 21
* Spring Boot 3.2.2
* Gradle 8.5
* Groovy 5.0

## Deployment

All of miro-services are deployed in docker containers via docker-compose

## Testing

All tests are written in Groovy using Spock Framework

For additional testing use https://github.com/AliakseiSayarkin/walty/blob/master/currency-exchange-service/[walty-postman-collection.json 
postman collection (note the localhost in every url)