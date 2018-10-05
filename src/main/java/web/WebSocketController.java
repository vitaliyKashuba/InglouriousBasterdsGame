package web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Controller
public class WebSocketController
{
    private final SimpMessagingTemplate template;

    @Autowired
    WebSocketController(SimpMessagingTemplate template){
        this.template = template;
    }

    @MessageMapping("/message")
    @SendTo("/topic/reply")
    public String processMessageBroadcas(@Payload String message) throws Exception {
//        String name = new Gson().fromJson(message, Map.class).get("name").toString();
        System.out.println("/message" + message);
        return "hello world";
    }

    @MessageMapping("/private_message")
    @SendToUser("/topic/reply")
    public String processMessagePrivate(@Payload String message) throws Exception {
//        String name = new Gson().fromJson(message, Map.class).get("name").toString();
        System.out.println("private" + message);
        template.convertAndSend("/topic/reply", "broadcast part");
        return "hello private world";
    }

}
