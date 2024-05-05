package net.bk24.macro.worker.infrastructure.client

import net.bk24.macro.common.RandomStringGenerator
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters

@Component
class WebClientHelper(private val httpHeaderProperties: HttpHeaderProperties) {
    fun setupHeaders(): MultiValueMap<String, String> {
        val headers = LinkedMultiValueMap<String, String>()
        headers.add(HttpHeaders.ACCEPT, httpHeaderProperties.accept)
        headers.add(HttpHeaders.CONNECTION, httpHeaderProperties.connection)
        headers.add(HttpHeaders.ACCEPT_ENCODING, httpHeaderProperties.acceptEncoding)
        headers.add(HttpHeaders.CONTENT_LENGTH, httpHeaderProperties.contentLength)
        headers.add(HttpHeaders.COOKIE, httpHeaderProperties.cookie)
        headers.add(HttpHeaders.CONTENT_TYPE, httpHeaderProperties.contentType)
        headers.add(HttpHeaders.USER_AGENT, httpHeaderProperties.userAgent)
        headers.add(HttpHeaders.HOST, httpHeaderProperties.host)
        headers.add(HttpHeaders.ACCEPT_LANGUAGE, httpHeaderProperties.acceptLanguage)
        return headers
    }

    companion object {
        fun createFormData(authCode: String, snsId: String = "", postNo: String = "", content: String = RandomStringGenerator.execute()): BodyInserters.FormInserter<String> {
            return BodyInserters.fromFormData("auth_code", authCode)
                .with("post_no", postNo)
                .with("sns_user_sn", snsId)
                .with("content", content)
                .with("season_level_join_flag", "Y")
                .with("season_level_sn", "1")
                .with("user_level", "47")
        }
    }
}
