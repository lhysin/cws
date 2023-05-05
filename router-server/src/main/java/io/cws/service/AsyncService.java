package io.cws.service;

import java.util.concurrent.CompletableFuture;

import io.cws.common.WebClientService;
import io.cws.sample.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import io.cws.client.CouponClient;
import io.cws.client.OrderClient;
import io.cws.client.PromotionClient;
import lombok.RequiredArgsConstructor;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncService {
    private final OrderClient orderClient;
    private final CouponClient couponClient;
    private final PromotionClient promotionClient;
    private final WebClientService webClientService;

    @Async
    public CompletableFuture<String> executeAsyncFeignClient() {
        CompletableFuture<Void> orderFuture = CompletableFuture.runAsync(orderClient::execute);
        CompletableFuture<Void> couponFuture = CompletableFuture.runAsync(couponClient::execute);
        CompletableFuture<Void> promotionFuture = CompletableFuture.runAsync(promotionClient::execute);

        return CompletableFuture.allOf(orderFuture, couponFuture, promotionFuture)
            .thenApply(unused -> "success");
    }

    public Disposable executeMultiApiCall() {
        return Flux.merge(
                this.getOrder(),
                this.getCoupon(),
                this.getPromotiion()
            )
            .parallel()
            .runOn(Schedulers.elastic())
            .ordered((o1, o2) -> o1.hashCode() - o2.hashCode())
            .log()
            .subscribe(o -> log.info(">>>>>> response : {}", o));
    }


    private Mono<ResponseDto> getOrder() {
        Mono<ResponseDto> responseDto = webClientService.requestPostWrap(
            "https://m7k6budzyd.execute-api.ap-northeast-2.amazonaws.com",
                "/default/item-serverless-dev-order_handler",
                null,
                "{\"offerCode\":\"AAA\"}"
            )
            .doOnSuccess(
                response -> log.info("[WebClient]Request apiCall Success. {}", response)
            )
            .doOnError(
                e -> {
                    log.error("[WebClient]Request apiCall Error. {}", e.getMessage());
                }
            )
            .onErrorComplete();
        return responseDto;
    }

    private Mono<ResponseDto> getCoupon() {
        Mono<ResponseDto> responseDto = webClientService.requestPostWrap(
            "https://4tttbhrut5.execute-api.ap-northeast-2.amazonaws.com",
                "/default/item-serverless-dev-coupon_handler",
                null,
                "{\"offerCode\":\"BBB\"}"
            )
            .doOnSuccess(
                response -> log.info("[WebClient]Request apiCall Success. {}", response)
            )
            .doOnError(
                e -> {
                    log.error("[WebClient]Request apiCall Error. {}", e.getMessage());
                }
            )
            .onErrorComplete();
        return responseDto;
    }

    private Mono<ResponseDto> getPromotiion() {
        Mono<ResponseDto> responseDto = webClientService.requestPostWrap(
            "https://wl7wfx08f3.execute-api.ap-northeast-2.amazonaws.com",
                "/default/item-serverless-dev-promotion_handler",
                null,
                "{\"offerCode\":\"CCC\"}"
            )
            .doOnSuccess(
                response -> log.info("[WebClient]Request apiCall Success. {}", response)
            )
            .doOnError(
                e -> {
                    log.error("[WebClient]Request apiCall Error. {}", e.getMessage());
                }
            )
            .onErrorComplete();
        return responseDto;
    }

}
