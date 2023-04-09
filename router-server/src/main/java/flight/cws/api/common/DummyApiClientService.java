package flight.cws.api.common;

import flight.cws.api.sample.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Slf4j
@Service
@RequiredArgsConstructor
public class DummyApiClientService {

    private final WebClientService webClientService;

    private static Mono<ResponseDto> accept(ResponseEntity<ResponseDto> clientResponseEntity) {
        log.info("Request apiCall Success. {}", clientResponseEntity.getBody());
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(clientResponseEntity.getBody().getStatus());
        responseDto.setData(clientResponseEntity.getBody().getData());
        return Mono.just(responseDto);
    }

    public Mono<ResponseDto> apiCall(String uriPath, String uriQuery) {

        Mono<ResponseDto> res = webClientService.requestGet(uriPath, uriQuery)
            .doOnSuccess(
                responseDto -> log.info("Request apiCall Success. {}", responseDto)
            )
            .doOnError(
                e -> {
                    log.error("response Error. {}",e.getMessage());
                    //throw new RuntimeException(e.getMessage());
                }
            )
            .onErrorComplete();

        return res;
    }


}
