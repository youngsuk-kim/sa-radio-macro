package net.bk24.macro.worker.application.writer

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.bk24.macro.common.ServerTodo
import net.bk24.macro.worker.application.CreateComment
import net.bk24.macro.worker.application.LogStore
import net.bk24.macro.worker.infrastructure.client.CommentClient
import net.bk24.macro.worker.infrastructure.client.WriteResponse
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.time.Instant

@Profile("live")
@Component
class CommentWriter(
    private val commentClient: CommentClient,
) : CreateComment {
    private val logger = LoggerFactory.getLogger("comment-writer")
    private val blockedSet = mutableSetOf<String>()

    override suspend fun execute(serverTodo: ServerTodo) {
        blockedSet.clear()
        LogStore.initialize()

        coroutineScope {
            launch {
                workUntilTimeOver(serverTodo)
            }.start()

            // 1분에 한 번씩 응답 개수 출력
        }
    }

    private suspend fun CommentWriter.workUntilTimeOver(serverTodo: ServerTodo) {
        val commentWriteStartTime = System.currentTimeMillis() // 시작 시간 기록
        val famousPostCommentTimeLimit = 28 * 60 * 1000 // 30분을 밀리초로 변환

        while (System.currentTimeMillis() - commentWriteStartTime < famousPostCommentTimeLimit) {
            sendCommentRequest(serverTodo.codes, serverTodo)
        }
        logger.info("time over")
    }

    private suspend fun sendCommentRequest(authCodes: List<String>, serverTodo: ServerTodo) {
        // Combine all authCode, snsId, and postNo pairs and shuffle them
        val requests = mutableListOf<Triple<String, String, String>>()
        for (authCode in authCodes) {
            serverTodo.snsIds.forEachIndexed { index, snsId ->
                requests.add(Triple(authCode, snsId, serverTodo.postNos[index].toString()))
            }
        }
        requests.shuffle()

        // Launch coroutines to process the shuffled requests
        coroutineScope {
            val lastRequestTime = mutableMapOf<String, Long>()
            val mutexMap = authCodes.associateWith { Mutex() }

            for ((authCode, snsId, postNo) in requests) {
                launch {
                    val mutex = mutexMap[authCode]
                    mutex?.withLock {
                        val now = Instant.now().toEpochMilli()
                        val lastTime = lastRequestTime[authCode] ?: 0L

                        if (now - lastTime >= 6000) {
                            lastRequestTime[authCode] = now
                            val res = commentClient.makeRequest(authCode, snsId, postNo)
                            logResponse(res, authCode)
                            logIfBlocked(res, authCode)
                            againIfCached(res)
                        } else {
                            delay(6000 - (now - lastTime)) // Wait and retry if not enough time has passed
                        }
                    }
                }
            }
        }
    }

    private fun againIfCached(res: WriteResponse?) {
        if (res == null) {
            return
        }
    }

    private suspend fun logIfBlocked(res: WriteResponse?, code: String) {
        if (res !== null && res.errorMessage.isBlank()) {
            blockedSet.add(code)
            logger.info("${blockedSet.size}")
            logger.info("$blockedSet")
        }
    }

    private suspend fun logResponse(res: WriteResponse?, code: String) {
        if (res !== null) {
            logger.info(code)
            logger.info(res.errorMessage)
        }
    }
}
