package io.cws.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import io.cws.sample.SampleService;
import io.cws.service.AsyncService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ItemConsumer {

    private final SampleService sampleService;
    private final AsyncService asyncService;

    @KafkaListener(topics = "${spring.kafka.template.default-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message) {
        sampleService.logAndReturnEmpty(
            Mono.just(message)
        ).then(
            Mono.fromFuture(asyncService.executeAsyncFeignClient())
        ).subscribe();
    }
}
