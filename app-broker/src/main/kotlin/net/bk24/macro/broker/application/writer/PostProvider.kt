package net.bk24.macro.broker.application.writer

import net.bk24.macro.broker.domain.repository.AuthRepository
import net.bk24.macro.broker.infrastructure.client.PostNoClient
import net.bk24.macro.broker.infrastructure.client.TaskClient
import net.bk24.macro.broker.infrastructure.client.WorkerIpProperties
import net.bk24.macro.common.ServerTodo
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class PostProvider(
    private val workerIpProps: WorkerIpProperties,
    private val authRepository: AuthRepository,
    private val postRecommendUpdater: PostRecommendUpdater,
) {
    private val logger = LoggerFactory.getLogger("post-provider")

    @Transactional(readOnly = true)
    fun execute() {
        logger.info("Sending post")
        val postWriter = authRepository
            .findAll()
            .filter { it.postId?.isNotBlank() == true }
            .map { it.code }
            .take(4)

        val lastIp = workerIpProps.ip

        val taskThread = Thread {
            try {
                repeat(lastIp.size) {
                    val todo = ServerTodo(
                        type = ServerTodo.Type.POST,
                        upNo = it,
                        writerId = postWriter[it],
                    )
                    TaskClient.execute(lastIp[it], todo)
                    logger.info("Task executed for IP: ${lastIp[it]}")
                    Thread.sleep(5000)
                }
            } catch (e: Exception) {
                logger.error("Error executing task", e)
            }
        }
        taskThread.start()
        taskThread.join()
        val code = authRepository.findAll().filter { !it.isBlocked }.take(30).map { it.code }
        val map = authRepository.findSnsIdNotNull().map { it.postId!! }
        val snsId = authRepository.findSnsIdNotNull().map { it.snsId!! }
        println(PostNoClient.getFirstPostInfo(map))
        val updateThread = Thread {
            try {
                repeat(4) {
                    println("좋아요시작")
                    postRecommendUpdater.updateRecommendations(
                        code,
                        PostNoClient.getFirstPostInfo(map)[it].postNo.toString(),
                        snsId[it],
                    )
                    logger.info("Recommendations updated for snsId: ${snsId[it]}")
                }
            } catch (e: Exception) {
                logger.error("Error updating recommendations", e)
            }
        }

        updateThread.start()
        updateThread.join()
    }
}
