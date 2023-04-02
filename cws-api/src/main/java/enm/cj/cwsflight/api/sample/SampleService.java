package enm.cj.cwsflight.api.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class SampleService {

    private final WebClient webClient;

    public SampleService(WebClient webClient) {
        this.webClient = webClient;
    }


    public Mono<String> getTestClient() {

        List<Integer> loop = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        loop.forEach(
                num -> {
                    log.info(">>>>>> Loop NUM : {}", num);

                    Mono<String> response = webClient.mutate().baseUrl("http://localhost:8080").build().get()
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
                            );

                    response.subscribe(str -> log.info(">>>>>> responseBody ReqNum-{} > Res : {}", num, str));
                }
        );

        return Mono.just("SUCCESS");

    }


}
