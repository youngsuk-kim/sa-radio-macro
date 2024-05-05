package net.bk24.macro.broker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class BrokerApplication

fun main(args: Array<String>) {
    runApplication<BrokerApplication>(*args)
}
