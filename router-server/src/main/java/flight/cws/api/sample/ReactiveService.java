package flight.cws.api.sample;

import flight.cws.api.common.DummyApiClientService;
import flight.cws.api.common.WebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactiveService {

    private final DummyApiClientService dummyApiClientService;
    private final WebClientService webClientService;

    public ResponseDto getResponseBlockTest() {

        return webClientService.requestBlockGet("/dummy/200", "param=ar");
    }


}
