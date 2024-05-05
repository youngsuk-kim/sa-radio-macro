package net.bk24.macro.broker.presentation

import net.bk24.macro.broker.application.writer.PostProvider
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TaskApi(
    private val postProvider: PostProvider,
) {
    @PostMapping("/sendTasks")
    fun sendTasks(): ResponseEntity<Void> {
        postProvider.execute()
        return ResponseEntity.ok().build()
    }
}
