package io.cws.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;

import io.cws.config.FeignConfig;

@FeignClient(name = "promotion-client", url = "https://wl7wfx08f3.execute-api.ap-northeast-2.amazonaws.com", configuration = FeignConfig.class)
public interface PromotionClient {

    @Async
    @PostMapping("/default/item-serverless-dev-promotion_handler")
    void execute();

}
