package net.bk24.macro.broker.application.writer

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

@Component
class PostRecommendUpdater {
    private val url = "https://saradioapi.nexon.com/Sns/PostRecommendUpdate.aspx"

    fun updateRecommendations(authCodes: List<String>, postNo: String, snsUserSn: String) {
        authCodes.forEach { authCode ->
            val headers = HttpHeaders().apply {
                set("Host", "saradioapi.nexon.com")
                set("Connection", "keep-alive")
                set("Cookie", "ASP.NET_SessionId=t03avlgbdb2p25fnbygzmdgl")
                set("Content-Type", "application/x-www-form-urlencoded")
                set("Accept-Language", "ko-KR;q=1, en-KR;q=0.9")
                set("User-Agent", "SuddenRadio/4.2.4 (iPhone; iOS 17.4.1; Scale/3.00)")
                set("Accept-Encoding", "gzip, deflate, br")
                set("Accept", "*/*")
            }

            val body = "auth_code=$authCode&post_no=$postNo&sns_user_sn=$snsUserSn"
            val entity = HttpEntity(body, headers)

            println("바디")
            println(body)

            val response = RestTemplate().exchange<String>(url, HttpMethod.POST, entity)

            println("Response for auth_code $authCode: ${response.body}")
        }
    }
}
