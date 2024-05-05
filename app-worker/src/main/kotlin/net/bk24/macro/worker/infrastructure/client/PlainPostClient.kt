package net.bk24.macro.worker.infrastructure.client

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

object PlainPostClient {
    fun createPost(authCode: String) {
        val url = "https://saradioapi.nexon.com/Sns/PostCreate.aspx"

        // Set up headers
        val headers = HttpHeaders().apply {
            accept = listOf(MediaType.ALL)
            add("Cookie", "ASP.NET_SessionId=xqcunqey3noinusd0cdsa1zo")
            add("Accept-Encoding", "gzip, deflate, br")
            add("Accept-Language", "ko-KR;q=1, en-KR;q=0.9")
            add("Connection", "keep-alive")
            add("User-Agent", "SuddenRadio/4.2.4 (iPhone; iOS 17.4.1; Scale/3.00)")
            add("Content-Length", "162")
            contentType = MediaType.APPLICATION_FORM_URLENCODED
        }

        // Set up request body
        val body = LinkedMultiValueMap<String, String>().apply {
            add("auth_code", authCode)
            add("content", "ㅎㅎ")
            add("hash_tag_1", "")
            add("hash_tag_2", "")
            add("hash_tag_3", "")
            add("post_type", "0")
            add("season_level_join_flag", "Y")
            add("season_level_no", "0")
            add("user_level", "0")
            add("youtube_url", "")
        }

        // Create the HttpEntity object
        val request = HttpEntity(body, headers)

        // Initialize RestTemplate
        val restTemplate = RestTemplate()

        // Send the request
        val response = restTemplate.exchange(url, HttpMethod.POST, request, String::class.java)

        println("Response: ${response.body}")
    }
}
