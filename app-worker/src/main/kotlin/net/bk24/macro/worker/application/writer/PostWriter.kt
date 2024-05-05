package net.bk24.macro.worker.application.writer

import PostClient.sendMultipartFormDataRequest
import net.bk24.macro.worker.application.CreatePost
import net.bk24.macro.worker.infrastructure.client.PlainPostClient
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap

@Profile("live", "dev")
@Component
class PostWriter : CreatePost {
    private val logger = LoggerFactory.getLogger("post-writer")

    private val comments = listOf(
        "번개처럼 빠른 SP 전달 번개SP \n오픈카톡 링크: http://pf.kakao.com/_AepmG",
        "업자가 더 손해봐서 운다 우니SP \n오픈카톡 링크: http://pf.kakao.com/_xgxgxcxmG",
        "손님에게 달콤함을 달콤SP \n오픈카톡 링크: http://pf.kakao.com/_exgxcxmG",
        "보보보라 저렴하고 신용있는 SP를 보보SP \n오픈카톡 링크: http://pf.kakao.com/_rxgxcxmG",
    )

    private val imageResources = listOf(
        ClassPathResource("cropped6219847526659768834.jpg"),
        ClassPathResource("cropped6219847526659768832.jpeg"),
        ClassPathResource("cropped6219847526659768833.jpeg"),
        ClassPathResource("cropped6219847526659768831.jpeg"),
    )

    override fun execute(authCode: String, index: Int) {
        PlainPostClient.createPost(authCode)

        Thread.sleep(3000)

        val url = "http://saradioapi.nexon.com/Sns/PostCreate.aspx"

        // 바운더리 생성
        val boundary = "6eKHrT-SN9MvpyapI3EiaKCXynkWZf"

        // Ensure index is within the valid range
        val safeIndex = index.coerceIn(0, comments.size - 1)
        val comment = comments[safeIndex]
        val imageResource = imageResources[safeIndex]

        // 요청 바디 생성
        val requestBody = LinkedMultiValueMap<String, Any>()
        requestBody.add("hash_tag_3", "")
        requestBody.add("hash_tag_2", "")
        requestBody.add("hash_tag_1", "")
        requestBody.add("post_type", "1")
        requestBody.add("season_level_no", "0")
        requestBody.add("youtube_url", "")
        requestBody.add("level_no", "0")
        requestBody.add("season_level_join_flag", "Y")
        requestBody.add("content", comment)
        requestBody.add("auth_code", authCode)

        // 이미지 파일을 바이트 배열로 읽어와서 requestBody에 추가
        requestBody.add(
            "post_image",
            object : ByteArrayResource(imageResource.inputStream.readBytes()) {
                override fun getFilename(): String {
                    return imageResource.filename ?: "image.jpg"
                }
            },
        )

        // 요청 보내기
        val sendMultipartFormDataRequest = sendMultipartFormDataRequest(url, requestBody, boundary)
        logger.info("게시물 작성 완료 $sendMultipartFormDataRequest")
    }
}
