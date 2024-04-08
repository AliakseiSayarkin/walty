package com.walty.telegram.web.telegram.command

import com.walty.telegram.web.telegram.command.CommandResolver
import com.walty.telegram.web.telegram.command.impl.CurrencyExchangeCommand
import com.walty.telegram.web.telegram.command.impl.DefaultResponseCommand
import com.walty.telegram.web.telegram.command.impl.GetTransactionRecordsCommand
import com.walty.telegram.web.telegram.command.impl.HelpCommand
import com.walty.telegram.web.telegram.command.impl.SaveTransactionRecordCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.util.ClassUtils
import spock.lang.Specification

@SpringBootTest
class CommandResolverIntegrationTest extends Specification {

    @Autowired
    CommandResolver commandResolver

    void "should get CurrencyExchangeCommand"() {
        given:
        var input = "/exchange"

        when:
        var command = commandResolver.resolve(input)

        then:
        CurrencyExchangeCommand.class == ClassUtils.getUserClass(command.getClass())
    }

    void "should get DefaultResponseCommand given unknown command"() {
        given:
        var input = "/unknown"

        when:
        var command = commandResolver.resolve(input)

        then:
        DefaultResponseCommand.class == ClassUtils.getUserClass(command.getClass())
    }

    void "should get HelpCommand given unknown command"() {
        given:
        var input = "/help"

        when:
        var command = commandResolver.resolve(input)

        then:
        HelpCommand.class == ClassUtils.getUserClass(command.getClass())
    }

    void "should get GetTransactionRecordsCommand given unknown command"() {
        given:
        var input = "/getTransactionRecords"

        when:
        var command = commandResolver.resolve(input)

        then:
        GetTransactionRecordsCommand.class == ClassUtils.getUserClass(command.getClass())
    }

    void "should get SaveTransactionRecordCommand given unknown command"() {
        given:
        var input = "/saveTransactionRecord"

        when:
        var command = commandResolver.resolve(input)

        then:
        SaveTransactionRecordCommand.class == ClassUtils.getUserClass(command.getClass())
    }
}
