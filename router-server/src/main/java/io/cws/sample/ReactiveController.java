package io.cws.sample;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReactiveController {

    private final ReactiveService reactiveService;

    @GetMapping(value = "/react/response/block")
    public Mono<ResponseDto> getResponseBlockTest() {

        //blocking으로 동작할때 별도 blocking 쓰레드로 사용해야함(reactor-http-nio 사용불가)
        Mono<ResponseDto> responseDto = Mono.fromCallable(
                () -> reactiveService.getResponseBlockTest()
            )
            .subscribeOn(Schedulers.boundedElastic())
            .log();
        return responseDto;
    }

    @GetMapping(value = "/react/response/nonblock")
    public Mono<Void> getResponseNonBlockTest() {
        reactiveService.getResponseNonBlockTest();
        return Mono.empty().then();
    }

    @GetMapping(value = "/react/response/multi")
    public Flux<Disposable> getResponseMultiTest() {

        return reactiveService.getMultiNonBlockTest1();
    }



}
