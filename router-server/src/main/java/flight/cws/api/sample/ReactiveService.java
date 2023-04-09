package flight.cws.api.sample;

import flight.cws.api.common.DummyApiClientService;
import flight.cws.api.common.WebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactiveService {

    private final DummyApiClientService dummyApiClientService;
    private final WebClientService webClientService;

    public ResponseDto getResponseBlockTest() {

        return webClientService.requestGetWrap("/dummy/200", "param=ar")
            .block();
    }

    public Mono<Void> getResponseNonBlockTest() {
        webClientService.requestGetWrap("/dummy/200", "param=ar")
            .subscribe(response -> log.info(">> response Status : {}, Body : {}", response.getStatus(), response.getData()));

        return Mono.empty();
    }

    public Flux<Disposable> getMultiNonBlockTest1() {
        return Flux.just(
            webClientService.requestGetWrap("/dummy/200", "param=1").subscribe(),
            webClientService.requestGetWrap("/dummy/200", "param=2").subscribe(),
            webClientService.requestGetWrap("/dummy/200", "param=3").subscribe()
        );
    }



}
