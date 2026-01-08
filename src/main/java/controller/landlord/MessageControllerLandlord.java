package controller.landlord;

import controller.MessageController;
import org.springframework.web.bind.annotation.RestController;
import service.MessageService;

@RestController
public class MessageControllerLandlord extends MessageController {
    public MessageControllerLandlord(service.MessageService service) {
        super(service);
    }
}
