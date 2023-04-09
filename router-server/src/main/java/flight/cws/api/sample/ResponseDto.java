package flight.cws.api.sample;

import lombok.Builder;
import lombok.Data;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

@Data
public class ResponseDto {
    private Integer status;
    private String message;
    private Object data;

}
