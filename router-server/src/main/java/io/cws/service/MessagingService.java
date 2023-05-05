package io.cws.service;

import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagingService {

    private final AmazonSQS amazonSQS;

    public Mono<SendMessageResult> createSqsMessage(String message) {
        log.debug("MessagingService.createSqsMessage() message : \n{}", message);
        return Mono.just(amazonSQS.sendMessage("https://sqs.ap-northeast-2.amazonaws.com/627500151784/std-dev-sqs-cws-item", message));
    }
}
