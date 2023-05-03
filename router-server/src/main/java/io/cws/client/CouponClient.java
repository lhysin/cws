package io.cws.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import io.cws.config.FeignConfig;

@FeignClient(name = "coupon-client", url = "https://4tttbhrut5.execute-api.ap-northeast-2.amazonaws.com", configuration = FeignConfig.class)
public interface CouponClient {

    @PostMapping("/default/item-serverless-dev-coupon_handler")
    void execute();

}
