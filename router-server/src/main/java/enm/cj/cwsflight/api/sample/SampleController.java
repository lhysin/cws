package enm.cj.cwsflight.api.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping(value = "/getResTest")
    public String getResTest(@RequestParam String param) throws InterruptedException {
        int ranVal = ThreadLocalRandom.current().nextInt(1000, 7000);
        Thread.sleep(ranVal);
        return "param:"+param +" / interval:" + String.valueOf(ranVal)+"ms";
    }
}
