package io.cws.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import io.cws.sample.SampleService;
import io.cws.service.AsyncService;
import io.cws.service.MessagingService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ItemConsumer {

    private final SampleService sampleService;
    private final AsyncService asyncService;
    private final MessagingService messagingService;

    @KafkaListener(topics = "${spring.kafka.template.default-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message) {
        // kafka 통해 수신된 메세지 로그 출력
        sampleService.logAndReturnEmpty(
            Mono.just(message)
        ).then(
            // 외부 API 동시 요청
            Mono.fromFuture(asyncService.executeAsyncFeignClient())
        ).then(
            // AWS SQS 메세지 생성
            messagingService.createSqsMessage(message)
        ).subscribe();
    }
}
