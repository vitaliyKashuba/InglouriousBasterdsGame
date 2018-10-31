package springApplication.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import springApplication.game.MessageSender;


@Controller
public class WebSocketController
{
    @Autowired
    MessageSender ms;

    @MessageMapping("/message/{id}")
    @SendTo("/topic/reply/{id}")
    public String processMessageBroadcas(@Payload String message, @DestinationVariable int id) throws Exception {
//        String name = new Gson().fromJson(message, Map.class).get("name").toString();
        System.out.println("/message" + message + " " + id);
        return "hello world";
    }

    @MessageMapping("/private_message")
    @SendToUser("/topic/reply")
    public String processMessagePrivate(@Payload String message) throws Exception {
//        String name = new Gson().fromJson(message, Map.class).get("name").toString();
        System.out.println("private" + message);
//        template.convertAndSend("/topic/reply", "broadcast part");

//        MessageSender ms = new MessageSender();
//        ms.sendBroadcast("123");

        return "hello private world";
    }

}
