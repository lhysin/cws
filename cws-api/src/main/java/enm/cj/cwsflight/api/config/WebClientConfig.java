package enm.cj.cwsflight.api.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import javax.net.ssl.SSLException;
import java.time.Duration;

@Slf4j
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {

        ConnectionProvider provider = getConnectionProvider();
        HttpClient httpClient = getHttpClient(provider);
        ExchangeStrategies exchangeStrategies = getExchangeStrategies();

        WebClient webClient = WebClient
                .builder()
                .clientConnector(
                        new ReactorClientHttpConnector(httpClient)
                )
                .exchangeStrategies(exchangeStrategies)
                .filter(
                        ExchangeFilterFunction.ofRequestProcessor(
                                clientRequest -> {
                                    log.debug("[WebClient]Request: {} {} ", clientRequest.method(), clientRequest.url());
                                    clientRequest
                                            .headers()
                                            .forEach(
                                                    (name, values) -> values.forEach(
                                                            value -> log.debug("[WebClient]RequestHeader:{} : {}", name, value)
                                                    )
                                    );
                                    return Mono.just(clientRequest);
                                }
                        )
                )
                .filter(
                        ExchangeFilterFunction.ofResponseProcessor(
                                clientResponse -> {
                                    clientResponse
                                            .headers()
                                            .asHttpHeaders()
                                            .forEach(
                                                    (name, values) -> values.forEach(
                                                            value -> log.debug("[WebClient]ResponseHeader:{} : {}", name, value)
                                                    )
                                            );
                                    return Mono.just(clientResponse);
                                }
                        )
                )
                .build();

        return webClient;
    }

    private static ExchangeStrategies getExchangeStrategies() {
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(
                        clientCodecConfigurer -> {
                            clientCodecConfigurer.defaultCodecs().maxInMemorySize(1024 * 1024 * 1); //메모리버퍼 1MB
                        }
                )
                .build();
        // 상세로그 확인을 위한 설정. level=debug 필요
        exchangeStrategies.messageWriters().stream()
                .filter(LoggingCodecSupport.class::isInstance)
                .forEach(
                        writer -> {
                            ((LoggingCodecSupport) writer).setEnableLoggingRequestDetails(true);
                        }
                );
        return exchangeStrategies;
    }

    private static HttpClient getHttpClient(ConnectionProvider provider) {
        HttpClient httpClient = HttpClient
                .create(provider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10_000)
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(10)) //읽기시간초과 타임아웃
                                .addHandlerLast(new WriteTimeoutHandler(10))
                )
                .responseTimeout(Duration.ofSeconds(10))
                .secure(
                        sslContextSpec -> {
                            try {
                                sslContextSpec.sslContext(
                                        SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build()
                                );
                            } catch (SSLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
        return httpClient;
    }

    private static ConnectionProvider getConnectionProvider() {
        ConnectionProvider provider = ConnectionProvider.builder("http-pool")
                .maxConnections(100)     //connection pool의 갯수
                .pendingAcquireTimeout(Duration.ofMillis(0)) //커넥션 풀에서 커넥션을 얻기 위해 기다리는 최대 시간
                .pendingAcquireMaxCount(-1) //커넥션 풀에서 커넥션을 가져오는 시도 횟수 (-1: no limit)
                .maxIdleTime(Duration.ofMillis(5000L)) //커넥션 풀에서 idle 상태의 커넥션을 유지하는 시간
                .maxLifeTime(Duration.ofSeconds(5000L)) //커넥션 풀에서 커넥션이 살아있을수 있는 최대 시간
                .build();
        return provider;
    }

}
