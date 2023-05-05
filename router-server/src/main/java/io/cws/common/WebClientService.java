package io.cws.common;

import io.cws.sample.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebClientService {

    private static final String BASE_URL = "http://localhost:8080";

    private final WebClient webClient;


    public Mono<ResponseDto> requestGetWrap(String uriPath, String uriQuery) {
        Mono<ResponseDto> responseDto = requestClient(HttpMethod.GET, null, uriPath, uriQuery, null)
            .doOnSuccess(
                response -> log.info("[WebClient]Request apiCall Success. {}", response)
            )
            .doOnError(
                e -> {
                    log.error("[WebClient]Request apiCall Error. {}", e.getMessage());
                    //throw new RuntimeException(e.getMessage());
                }
            )
            .onErrorComplete();
        return responseDto;
    }

    public Mono<ResponseDto> requestPostWrap(String baseUrl, String uriPath, String uriQuery, Object bodyData) {
        Mono<ResponseDto> responseDto = requestClient(HttpMethod.POST, baseUrl, uriPath, uriQuery, bodyData)
            .doOnSuccess(
                response -> log.info("[WebClient]Request apiCall Success. {}", response)
            )
            .doOnError(
                e -> {
                    log.error("[WebClient]Request apiCall Error. {}", e.getMessage());
                    //throw new RuntimeException(e.getMessage());
                }
            )
            .onErrorComplete();
        return responseDto;
    }

    public Mono<ResponseDto> requestClient(HttpMethod httpMethod, String baseUrl, String uriPath, String uriQuery, Object bodyData) {

        String targetDomain = Optional.ofNullable(baseUrl)
            .orElse(BASE_URL);

        WebClient.ResponseSpec resposeSpec = httpMethod.matches("POST") ? postMethodResponseSpec(targetDomain, uriPath, uriQuery, bodyData) : getMethodResponseSpec(targetDomain, uriPath, uriQuery);

        Mono<ResponseDto> responseDtoMono = resposeSpec
            .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                log.error("[WebClient]Request External Url is 5xx ERROR. {}, {}, {}", clientResponse.rawStatusCode(), clientResponse.bodyToMono(String.class), clientResponse.headers().asHttpHeaders());
                return Mono.error(
                    new WebClientResponseException(
                        clientResponse.rawStatusCode(),
                        "External Url is 5xx ERROR.",
                        clientResponse.headers().asHttpHeaders(),
                        null,
                        null
                    )
                );
            })
            .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                log.error("[WebClient]Request External Url is 4xx ERROR. {}, {}, {}", clientResponse.rawStatusCode(), clientResponse.bodyToMono(String.class), clientResponse.headers().asHttpHeaders());
                return Mono.error(
                    new WebClientResponseException(
                        clientResponse.rawStatusCode(),
                        "External Url is 4xx ERROR.",
                        clientResponse.headers().asHttpHeaders(),
                        null,
                        null
                    )
                );
            })
            .bodyToMono(
                new ParameterizedTypeReference<ResponseDto>() {
                }
            )
            .log();
        //.toEntity(ResponseDto.class);

        return responseDtoMono;
    }

    private WebClient.ResponseSpec getMethodResponseSpec(String targetDomain, String uriPath, String uriQuery) {
        WebClient.ResponseSpec resposeSpec = webClient.mutate().baseUrl(targetDomain).build()
            .get()
            .uri(
                uriBuilder -> uriBuilder
                    .path(uriPath)
                    .query(uriQuery)
                    .build()
            )
            .accept(MediaType.APPLICATION_JSON)
            .retrieve();

        return resposeSpec;
    }

    private WebClient.ResponseSpec postMethodResponseSpec(String targetDomain, String uriPath, String uriQuery, Object bodyData) {
        WebClient.ResponseSpec resposeSpec = webClient.mutate().baseUrl(targetDomain).build()
            .post()
            .uri(
                uriBuilder -> uriBuilder
                    .path(uriPath)
                    .query(uriQuery)
                    .build()
            )
            .bodyValue(bodyData)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve();

        return resposeSpec;
    }

}
