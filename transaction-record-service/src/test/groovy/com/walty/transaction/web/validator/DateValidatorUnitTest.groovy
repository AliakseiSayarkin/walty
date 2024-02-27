package com.walty.transaction.web.validator

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class DateValidatorUnitTest extends Specification {

    @Autowired
    DateValidator dateValidator

    void "should validate date"() {
        given:
        var date = new Date()

        when:
        dateValidator.validate(date)

        then:
        noExceptionThrown()
    }

    void "should throw NullPointerException when validate() given null date"() {
        when:
        dateValidator.validate(null)

        then:
        thrown NullPointerException
    }

    void "should throw IllegalArgumentException when validate() given future date"() {
        when:
        dateValidator.validate(getOneHourInTheFuture())

        then:
        thrown IllegalArgumentException
    }

    Date getOneHourInTheFuture() {
        var calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR_OF_DAY, 1)
        return calendar.getTime()
    }
}
