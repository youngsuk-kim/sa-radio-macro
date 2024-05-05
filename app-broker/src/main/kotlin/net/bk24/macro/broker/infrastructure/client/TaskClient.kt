package net.bk24.macro.broker.infrastructure.client

import net.bk24.macro.common.ServerTodo
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.web.client.RestTemplate

object TaskClient {

    fun execute(ip: String, serverTodo: ServerTodo) {
        val restClient = RestTemplate()
        val url = "http://$ip/task/execute"
        val headers = HttpHeaders()
        headers.add("Content-Type", "application/json")
        val httpEntity = HttpEntity(serverTodo, headers)
        restClient.postForEntity(url, httpEntity, String::class.java)
        println("Task executed: $serverTodo")
    }
}
