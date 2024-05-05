package net.bk24.macro.worker.infrastructure.client

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "http.headers")
class HttpHeaderProperties(
    var accept: String = "",
    var connection: String = "",
    var acceptEncoding: String = "",
    var contentType: String = "",
    var userAgent: String = "",
    var host: String = "",
    var acceptLanguage: String = "",
    var cookie: String = "",
    var contentLength: String = "",
)
