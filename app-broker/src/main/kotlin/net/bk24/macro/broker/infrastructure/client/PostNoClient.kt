package net.bk24.macro.broker.infrastructure.client

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

data class PostInfo(val snsId: String, val postNo: Int)

object PostNoClient {
    fun getFirstPostInfo(postIds: List<String>): List<PostInfo> {
        val restTemplate = RestTemplate()
        val objectMapper = jacksonObjectMapper().registerKotlinModule()
        val results = mutableListOf<PostInfo>()

        postIds.forEach { postId ->
            val headers = HttpHeaders().apply {
                contentType = MediaType.APPLICATION_FORM_URLENCODED
                accept = listOf(MediaType.ALL)
                add("Cookie", "ASP.NET_SessionId=huxwskhkhqobcansnll3g5e4")
                add("Connection", "keep-alive")
                add("Accept-Encoding", "gzip, deflate, br")
                add("Content-Length", "83")
                add("User-Agent", "SuddenRadio/4.2.4 (iPhone; iOS 17.4.1; Scale/3.00)")
                add("Host", "saradioapi.nexon.com")
                add("Accept-Language", "ko-KR;q=1, en-KR;q=0.9")
            }

            val formParams = LinkedMultiValueMap<String, String>().apply {
                add("currentPage", "1")
                add("pageSize", "1") // Change pageSize to 1 to get only the first post
                add("select_type", "user")
                add("tag", "")
                add("user_nexon_sn", postId)
                add("user_type", "")
            }

            val request = HttpEntity(formParams, headers)

            val response = restTemplate.exchange(
                "https://saradioapi.nexon.com/Sns/PostList.aspx",
                HttpMethod.POST,
                request,
                String::class.java,
            ).body ?: return@forEach

            val rootNode = objectMapper.readTree(response)
            val dataset = rootNode.path("Dataset")

            if (dataset.isArray && dataset.size() > 0) {
                val firstPost = dataset[0]
                val snsId = firstPost.path("snsid").asText()
                val postNo = firstPost.path("post_no").asInt()
                results.add(PostInfo(snsId, postNo))
            }
        }

        return results
    }
}
