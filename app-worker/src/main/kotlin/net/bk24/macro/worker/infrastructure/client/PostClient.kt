import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import java.net.URI

object PostClient {
    fun sendMultipartFormDataRequest(
        url: String,
        requestBody: LinkedMultiValueMap<String, Any>,
        boundary: String,
    ): String {
        val restTemplate = RestTemplate()

        // 헤더 설정
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.MULTIPART_FORM_DATA
        httpHeaders.set("boundary", boundary)
        httpHeaders.set("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 10; SM-N960N Build/QP1A.190711.020)")
        httpHeaders.set("Host", "saradioapi.nexon.com")
        httpHeaders.set("Connection", "Keep-Alive")
        httpHeaders.set("Accept-Encoding", "gzip")

        // 요청 바디와 헤더를 RequestEntity로 묶기
        val requestEntity = RequestEntity(
            requestBody,
            httpHeaders,
            HttpMethod.POST,
            URI.create(url),
        )

        // POST 요청 보내기
        return restTemplate.exchange(requestEntity, String::class.java).body ?: ""
    }
}
