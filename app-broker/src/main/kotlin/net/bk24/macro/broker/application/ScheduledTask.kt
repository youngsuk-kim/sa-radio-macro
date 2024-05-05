package net.bk24.macro.broker.application

import jakarta.annotation.PostConstruct
import net.bk24.macro.broker.application.writer.CommentProvider
import net.bk24.macro.broker.application.writer.PostProvider
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

private const val ONE_MINUTE = 60 * 1000L

private const val START_TIME = 29

/**
 * !주의 29분 이후에 실행시키지 말것!
 */

@Component
class ScheduledTask(
    private val postProvider: PostProvider,
    private val commentProvider: CommentProvider,
) {

    @PostConstruct
    fun scheduleTask() {
        val now = LocalDateTime.now()
        val targetTime = now.withMinute(START_TIME).withSecond(10).withNano(0)

        val initialDelay = if (now.minute >= START_TIME) {
            // 시간이 지난 경우, 다음 시간을 예약
            targetTime.plusHours(1).atZone(ZoneId.systemDefault()).toEpochSecond() - now.atZone(ZoneId.systemDefault())
                .toEpochSecond()
        } else {
            // 아직 시간이 지나지 않은 경우, 현재 시간부터 타겟 시간까지의 초
            targetTime.atZone(ZoneId.systemDefault()).toEpochSecond() - now.atZone(ZoneId.systemDefault())
                .toEpochSecond()
        }

        val executor = Executors.newSingleThreadScheduledExecutor()
        executor.schedule(
            {
                executeTask()
            },
            initialDelay,
            TimeUnit.SECONDS,
        )
    }

    fun executeTask() {
        postProvider.execute()
        Thread.sleep(ONE_MINUTE * 30)
        while (true) {
            postProvider.execute()
            Thread.sleep(ONE_MINUTE * 1)
            commentProvider.execute()
            Thread.sleep(ONE_MINUTE * 29)
            postProvider.execute()
            Thread.sleep(ONE_MINUTE * 1)
            commentProvider.execute()
            Thread.sleep(ONE_MINUTE * 29)
        }
    }
}
