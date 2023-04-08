package flight.cws.api.sample;

import lombok.Data;

@Data
public class ResponseDto {
    private String status;

    private String message;
    private Object data;
}
