package flight.cws.api.common;

import flight.cws.api.sample.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebClientService {

    private static final String BASE_URL = "http://localhost:8080";

    private final WebClient webClient;

    public Mono<ResponseDto> requestGet(String uriPath, String uriQuery) {

        Mono<ResponseDto> responseDtoMono = webClient.mutate().baseUrl(BASE_URL).build()
            .get()
            .uri(
                uriBuilder -> uriBuilder
                    .path(uriPath)
                    .query(uriQuery)
                    .build()
            )
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
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
                new ParameterizedTypeReference<ResponseDto>() {}
            );
            //.toEntity(ResponseDto.class);

        return responseDtoMono;
    }

}
