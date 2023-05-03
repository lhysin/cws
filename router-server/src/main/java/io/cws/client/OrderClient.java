package io.cws.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import io.cws.config.FeignConfig;

@FeignClient(name = "order-client", url = "https://m7k6budzyd.execute-api.ap-northeast-2.amazonaws.com", configuration = FeignConfig.class)
public interface OrderClient {

    @PostMapping("/default/item-serverless-dev-order_handler")
    void execute();
}
