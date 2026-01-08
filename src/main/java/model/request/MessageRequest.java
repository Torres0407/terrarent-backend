package model.request;

import lombok.Data;
import java.util.UUID;

@Data
public class MessageRequest {

    private UUID recipientId;
    private String content;
    private UUID bookingId;

}
