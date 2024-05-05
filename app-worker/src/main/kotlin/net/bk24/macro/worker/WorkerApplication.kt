package net.bk24.macro.worker

import net.bk24.macro.worker.infrastructure.client.GetLocalIpAddress
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WorkerApplication

fun main(args: Array<String>) {
    System.setProperty("PUBLIC_IP", GetLocalIpAddress.execute())
    runApplication<WorkerApplication>(*args)
}
