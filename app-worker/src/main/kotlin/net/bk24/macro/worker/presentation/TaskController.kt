package net.bk24.macro.worker.presentation

import kotlinx.coroutines.reactor.asFlux
import net.bk24.macro.common.ServerTodo
import net.bk24.macro.worker.application.CreateComment
import net.bk24.macro.worker.application.CreatePost
import net.bk24.macro.worker.application.LogStore
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class TaskController(
    private val createComment: CreateComment,
    private val createPost: CreatePost,
) {
    private val logger = LoggerFactory.getLogger("task-controller")

    @PostMapping("/task/execute")
    suspend fun executeTask(@RequestBody serverTodo: ServerTodo): ResponseEntity<Void> {
        logger.info("Received task from server $serverTodo")

        when (serverTodo.type) {
            ServerTodo.Type.POST -> {
                createPost.execute(serverTodo.writerId, serverTodo.upNo)
            }

            ServerTodo.Type.COMMENT -> {
                try {
                    createComment.execute(serverTodo)
                } catch (e: Exception) {
                    println(e)
                }
            }
        }
        return ResponseEntity.ok().build()
    }

    @GetMapping(value = ["/task/logs"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun streamLogs(): Flux<Int> {
        return LogStore.logFlow.asFlux() // Flow를 Flux로 변환
    }
}
