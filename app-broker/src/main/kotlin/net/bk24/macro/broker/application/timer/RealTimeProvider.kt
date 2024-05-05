package net.bk24.macro.broker.application.timer

import net.bk24.macro.common.Write
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

/**
 * 01 : 29분 게시물 작성
 * ㅡㅡㅡㅡ루프ㅡㅡㅡㅡ
 * (1)01 : 59분 게시물 작성
 * (2)02 : 00분 댓글작성 { 첫번째 포스트 }
 * (3)02 : 29분 게시물 작성
 * (4)02 : 30분 댓글작성 { 두번째 포스트 }
 * --------------
 * (1)02 : 59분 게시물 작성
 * (2)03 : 00분 댓글작성 { 세번째 포스트 }
 * (3)03 : 29분 게시물 작성
 * (4)03 : 30분 댓글작성 { 네번째 포스트 }
 * 반복
 */

@Profile("live")
@Component
class RealTimeProvider : TimeProvider {
    override fun isTimeToRun(currentTime: Date): Write? {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
        calendar.time = currentTime

        val currentMinute = calendar.get(Calendar.MINUTE)

        return when (currentMinute) {
            29, 59 -> Write.POST
            0, 30 -> Write.COMMENT
            else -> return null
        }
    }
}
