package net.bk24.macro.broker.application.writer

import net.bk24.macro.broker.domain.repository.AuthRepository
import net.bk24.macro.broker.infrastructure.client.PostInfo
import net.bk24.macro.broker.infrastructure.client.PostNoClient
import net.bk24.macro.broker.infrastructure.client.TaskClient
import net.bk24.macro.broker.infrastructure.client.WorkerIpProperties
import net.bk24.macro.common.ServerTodo
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private const val CHUNK_SIZE = 10

@Service
class CommentProvider(
    private val authRepository: AuthRepository,
    private val workerIpProps: WorkerIpProperties,
) {
    private val logger = LoggerFactory.getLogger("comment-provider")

    @Transactional(readOnly = true)
    fun execute() {
        val auth = authRepository.findAll()
        val postWriters = auth
            .filter { it.postId?.isNotBlank() == true }
            .map { PostWriter(it.snsId!!, it.postId!!, it.code) }

        val ips = workerIpProps.ip

        for (ip in ips) {
            val index = ips.indexOf(ip)
            Thread {
                val postNos = PostNoClient.getFirstPostInfo(postWriters.map { it.postId })
                    .map { it }

                sendRequestToWorker(postWriters.map { it.snsId }, ip, postNos, index)
                Thread.sleep(2000)
            }.start()
        }
    }

    private fun sendRequestToWorker(snsids: List<String>, ip: String, postNos: List<PostInfo>, serverIndex: Int) {
        logger.info("Sending request to $ip")

        val serverTodo = ServerTodo(
            codes = authRepository.findActiveCodesByChunkSize(CHUNK_SIZE, serverIndex),
            snsIds = snsids,
            postNos = postNos.map { it.postNo - 2 },
        )
        TaskClient.execute(ip, serverTodo)
    }

    private fun AuthRepository.findActiveCodesByChunkSize(chunkSize: Int, index: Int): List<String> {
        return findAll()
            .filter { !it.isBlocked }
            .map { it.code }
            .chunked(chunkSize)
            .getOrElse(index) { emptyList() }
    }

    companion object {
        data class PostWriter(
            val snsId: String,
            val postId: String,
            val code: String,
        )
    }
}
