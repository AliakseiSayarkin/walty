# Walty – Expense Tracking Telegram Bot

## Overview

This is telegram bot that is used to track daily expenses and current exchange rates.

## Commands

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

  #### Response if no transaction records were found

        No transaction records found

* ### Save Transaction Record

  #### Description:

  Saves transaction record

  #### Command

      /saveTransactionRecord {currency code} {value} {-d description} (-d description is optional)

  #### Response

      Transaction record is saved

  #### Responses if command parameters are invalid

    *      No value found, please use /saveTransactionRecord {currency code} {value} (for example 100)
    *      No currency code found, please use /saveTransactionRecord {currency code} (for example USD) {value}
    *      Description starts with -d and should be last in a command

* ### Delete All Transaction Records

  #### Description:

  Deletes all transaction records for a given user

  #### Command

        /deleteAllTransactionRecords

  #### Response

        All transaction records were successfully deleted

* ### Delete Transaction Record By Id

  #### Description:

  Deletes transaction record for a given user by id

  #### Command

        /deleteTransactionRecordById {id}

  #### Response

        Transaction record with id: 6612b6c799567144919d1698 successfully deleted
  #### Responses if command parameters are invalid

    *      No transaction record id found, please use /deleteTransactionRecordById {transaction record id} (for example 1)
    *      Transaction record id must be a single word, please use /deleteTransactionRecordById {transaction record id} (for example 1)

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

  #### Responses if command parameters are invalid

    *      No currency code found, please use /exchange {currency code} (for example USD)

* ### Help

  #### Description:

  Returns all available commands

  #### Command

        /help

  #### Response

        Available commands:
        /getTransactionRecords - get all transaction records
        /saveTransactionRecord - save transaction record
        /deleteAllTransactionRecords - delete all transaction records
        /deleteTransactionRecordById - delete transaction record record by id
        
        /exchange - get real-time currency exchange rates
        
        /help - get all available commands with their description

* ### Invalid Command
  #### Description:

  When invalid command is given bot returns promp with help command

  #### Response
        No currency code found, please use /exchange {currency code} (for example USD)

## Dependencies

* Java 21
* Spring Boot 3.2.2
* Gradle 8.5
* Groovy 5.0
