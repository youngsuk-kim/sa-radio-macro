package net.bk24.macro.worker.infrastructure.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.benmanes.caffeine.cache.Cache
import kotlinx.coroutines.reactive.awaitSingle
import net.bk24.macro.worker.infrastructure.client.WebClientHelper.Companion.createFormData
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import java.time.Instant

@Component
class CommentClient(
    private val webClient: WebClient,
    private val webClientHelper: WebClientHelper,
    private val authCodeCache: Cache<String, Long>,
    private val objectMapper: ObjectMapper,
) {
    private val logger = LoggerFactory.getLogger("comment-client")

    suspend fun makeRequest(
        authCode: String,
        snsId: String,
        postNo: String,
    ): WriteResponse? {
        val now = Instant.now().toEpochMilli()
        val cacheKey = authCode
        val lastTime = authCodeCache.getIfPresent(cacheKey)

        // 최소 요청 간격 확인 (6000ms 이상)
        if (lastTime != null && (now - lastTime) < 6000) {
//            logger.info("Request too soon after last request for $postNo. Skipping.")
            return null
        }

        // 캐시 업데이트
        authCodeCache.put(cacheKey, now)
        val formData = createFormData(authCode, snsId, postNo)

        println("snsId: $snsId postNo: $postNo")

        return try {
            webClient.post()
                .uri("/Sns/PostCommentCreate.aspx")
                .headers { headers -> headers.addAll(webClientHelper.setupHeaders()) }
                .body(formData)
                .exchangeToMono { it.bodyToMono(String::class.java) }
                .map { objectMapper.readValue(it, WriteResponse::class.java) }
                .awaitSingle()
        } catch (e: Exception) {
            logger.error("Exception caught during web request: ${e.message}", e)
            null
        }
    }
}
