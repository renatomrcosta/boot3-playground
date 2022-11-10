package com.example.boot3playground

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import reactor.core.publisher.Mono

@SpringBootApplication
class Boot3PlaygroundApplication

fun main(args: Array<String>) {
    runApplication<Boot3PlaygroundApplication>(*args)
}

@RestController
class Controllo(
    private val reactorClient: ReactorClient,
    private val coroutinesClient: CoroutinesClient
) {
    @GetMapping("/coroutines/hello")
    suspend fun hello(): String {
        println("starting")
        val withDelay = coroutinesClient.getWithDelay()
        println("finishing")
        return withDelay
    }

    @GetMapping("/reactor/hello")
    suspend fun helloReactor(): String {
        println("starting")
        val withDelay = reactorClient.getWithDelay().awaitSingle()
        println("finishing")
        return withDelay
    }
}

@Configuration
class Configuration {
    @Bean
    fun httpBinWebClient(): WebClient = WebClient.builder().baseUrl("https://httpbin.org/").build()

    @Bean
    fun proxy(httpBinWebClient: WebClient): HttpServiceProxyFactory = WebClientAdapter.createHttpServiceProxyFactory(
        httpBinWebClient
    )

    @Bean
    fun coroutinesClient(proxy: HttpServiceProxyFactory): CoroutinesClient =
        proxy.createClient(CoroutinesClient::class.java)

    @Bean
    fun reactorClient(proxy: HttpServiceProxyFactory): ReactorClient = proxy.createClient(ReactorClient::class.java)
}

interface CoroutinesClient {
    @GetExchange("/delay/5")
    suspend fun getWithDelay(): String
}

interface ReactorClient {
    @GetExchange("/delay/5")
    fun getWithDelay(): Mono<String>
}
