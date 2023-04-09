package flight.cws.api.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RestController
public class SampleController {

    private final SampleService sampleService;

    public SampleController(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    @GetMapping(value = "/test")
    public Mono<String> getTest() {
        return sampleService.getTestClient();
    }

    @GetMapping(value = "/newTest")
    public Disposable getNewTest() {
        Disposable disposable = sampleService.getNewTest();
        return disposable;
    }

    @GetMapping(value = "/getResTest")
    public ResponseEntity<ResponseDto> getResTest(@RequestParam String param) throws InterruptedException {
        int ranVal = ThreadLocalRandom.current().nextInt(1000, 7000);
        Thread.sleep(ranVal);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(HttpStatus.OK.value());
        responseDto.setMessage(HttpStatus.OK.getReasonPhrase());
        responseDto.setData("param:"+param +" / interval:" + String.valueOf(ranVal)+"ms");
        log.info(">>> To Dummy REQUEST : {}", responseDto);
        return ResponseEntity.ok(responseDto);
    }
}
