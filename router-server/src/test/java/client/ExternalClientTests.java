package client;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.cws.RouterServerApplication;
import io.cws.service.AsyncService;

@SpringBootTest(classes = RouterServerApplication.class)
public class ExternalClientTests {

    private static final Logger log = LoggerFactory.getLogger(ExternalClientTests.class);

    @Autowired
    AsyncService asyncService;

    @Test
    void should_success() {
        String str = asyncService.executeAsyncFeignClient().join();
        log.debug(str);
    }

}
