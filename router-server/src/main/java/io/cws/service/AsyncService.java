package io.cws.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import io.cws.client.CouponClient;
import io.cws.client.OrderClient;
import io.cws.client.PromotionClient;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AsyncService {
    private final OrderClient orderClient;
    private final CouponClient couponClient;
    private final PromotionClient promotionClient;

    @Async
    public CompletableFuture<String> executeAsyncFeignClient() {
        CompletableFuture<Void> orderFuture = CompletableFuture.runAsync(orderClient::execute);
        CompletableFuture<Void> couponFuture = CompletableFuture.runAsync(couponClient::execute);
        CompletableFuture<Void> promotionFuture = CompletableFuture.runAsync(promotionClient::execute);

        return CompletableFuture.allOf(orderFuture, couponFuture, promotionFuture)
            .thenApply(unused -> "success");
    }
}
