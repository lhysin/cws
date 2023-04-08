package flight.cws.api.sample;

import flight.cws.api.common.DummyApiClientService;
import flight.cws.api.common.WebClientService;
import io.netty.handler.timeout.ReadTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.net.http.HttpTimeoutException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SampleService {

    private final WebClient webClient;
    private final DummyApiClientService dummyApiClientService;
    private final WebClientService webClientService;


    public Mono<String> getTestClient() {

        List<Integer> loop = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        loop.forEach(
            num -> {
                log.info(">>>>>> Loop NUM : {}", num);

                Mono<String> response = webClient.mutate().baseUrl("http://localhost:8080").build()
                    .get()
                    .uri(
                        uriBuilder -> uriBuilder
                            .path("/getResTest")
                            .queryParam("param", num)
//                                            .queryParam("color", "{authorId}")
//                                            .queryParam("deliveryDate", "{date}")
                            .build()
                    )
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnSuccess(
                        respose -> {
                            log.debug("ResponseBody : {}", respose);
                        }
                    )
                    .doOnError(
                        e -> {
                            log.error("response Error. {}",e.getMessage());
                            throw new RuntimeException(e.getMessage());
                        }
                    );

                response.subscribe(str -> log.info(">>>>>> responseBody ReqNum-{} > Res : {}", num, str));
            }
        );

        return Mono.just("SUCCESS");

    }

    public Disposable getNewTest() {
        Disposable disposable = dummyApiClientService.apiCall("/getResTest", "param=2")
            .onErrorMap(ReadTimeoutException.class, ex -> new HttpTimeoutException("ReadTimeout"))
            .subscribe(response -> log.info(">>>>>> response Status : {}, Body : {}", response.getStatus(), response.getData())
            );

        webClientService.requestGet("/getResTest", "param=1")
            .onErrorMap(ReadTimeoutException.class, ex -> new HttpTimeoutException("ReadTimeout"))
            .subscribe(response -> log.info(">>>>>> response Status : {}, Body : {}", response.getStatus(), response.getData())
            );

        return disposable;
    }


}
