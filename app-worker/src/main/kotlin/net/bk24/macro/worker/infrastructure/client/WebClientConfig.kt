package net.bk24.macro.worker.infrastructure.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.time.Duration
import java.util.concurrent.TimeUnit

@Configuration
class WebClientConfig {
    @Bean
    fun authCodeCache(): Cache<String, Long> {
        return Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.DAYS)
            .build()
    }

    @Bean
    fun httpHeaderProperties(): HttpHeaderProperties {
        return HttpHeaderProperties()
    }

    @Bean
    fun webClient(objectMapper: ObjectMapper): WebClient {
        return WebClient.builder()
            .baseUrl("https://saradioapi.nexon.com")
            .codecs { configurer -> configurer.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper)) }
//            .filter(logResponse())
            .clientConnector(ReactorClientHttpConnector(httpClient()))
            .build()
    }

    @Bean
    fun httpClient(): HttpClient {
        return HttpClient
            .create()
//            .proxy {
//                it.type(ProxyProvider.Proxy.HTTP)
//                    .host("proxy.proxy-cheap.com") // 프록시 서버의 주소
//                    .port(31112) // 프록시 서버의 포트
//                    .username("ftukdh3x")
//                    .password { "TtOK1y9VqwcQWuKK" }
//            }
            .doOnConnected { conn ->
                conn.addHandlerLast(ReadTimeoutHandler(0))
                conn.addHandlerLast(WriteTimeoutHandler(0))
            }.responseTimeout(Duration.ofSeconds(0))
    }

    @Bean
    fun connectionProvider(): ConnectionProvider {
        return ConnectionProvider.builder("customConnectionProvider").maxConnections(100)
            .maxIdleTime(Duration.ofMinutes(10)).maxLifeTime(Duration.ofMinutes(30)).build()
    }

    fun logResponse(): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofResponseProcessor { clientResponse ->
            println("Response status: ${clientResponse.statusCode()}")
            clientResponse.headers().asHttpHeaders().forEach { name, values ->
                values.forEach { value ->
                    println("Header: $name=$value")
                }
            }
            // 응답 바디 로깅 (비동기적으로 처리)
            clientResponse.bodyToMono(String::class.java).subscribe { body ->
                println("Body: $body")
            }
            Mono.just(clientResponse)
        }
    }
}
