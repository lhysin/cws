package flight.cws.api.sample;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class DummyController {

    @GetMapping(value = "/dummy/200")
    public ResponseEntity<ResponseDto> response200(@RequestParam(required = false) String param) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(HttpStatus.OK.value());
        responseDto.setMessage("Request Success.");
        responseDto.setData("param : "+ Optional.ofNullable(param).orElse("empty:-_-;"));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/dummy/400")
    public ResponseEntity<ResponseDto> response400(@RequestParam(required = false) String param) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(HttpStatus.BAD_REQUEST.value());
        responseDto.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        responseDto.setData("param : "+ Optional.ofNullable(param).orElse("empty:-_-;"));
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/dummy/500")
    public ResponseEntity<ResponseDto> response500(@RequestParam(required = false) String param) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        responseDto.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        responseDto.setData("param : "+ Optional.ofNullable(param).orElse("empty:-_-;"));
        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(value = "/dummy/timeout")
    public ResponseEntity<ResponseDto> responseTimeout(@RequestParam(required = false) String param) throws InterruptedException {
        Thread.sleep(10_000);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(HttpStatus.OK.value());
        responseDto.setMessage(HttpStatus.OK.getReasonPhrase());
        responseDto.setData("param : "+ Optional.ofNullable(param).orElse("empty:-_-;"));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/dummy/randTimeout")
    public ResponseEntity<ResponseDto> responseRandTimeout(@RequestParam(required = false) String param) throws InterruptedException {
        int ranVal = ThreadLocalRandom.current().nextInt(1_000, 7_000);
        Thread.sleep(ranVal);

        HashMap map = new HashMap<>();
        map.put("param", Optional.ofNullable(param).orElse("empty:-_-;"));
        map.put("interval", ranVal+"ms");

        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus(HttpStatus.OK.value());
        responseDto.setMessage(HttpStatus.OK.getReasonPhrase());
        responseDto.setData(map);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
