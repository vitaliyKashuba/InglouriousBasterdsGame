package springApplication.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import springApplication.game.MessageSender;

import java.security.Principal;


@Controller
public class WebSocketController
{
    @Autowired
    MessageSender ms;

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/message/{id}")
    @SendTo("/topic/reply/{id}")
    public String processMessageBroadcas(Principal principal, @Payload String message, @DestinationVariable int id) throws Exception {
//        String name = new Gson().fromJson(message, Map.class).get("name").toString();
//        System.out.println(sha);
//        System.out.println(sha.getSessionId());
//        System.out.println(sha.getSessionAttributes());
        System.out.println(principal.getName());
        System.out.println("/message" + message + " " + id);
        template.convertAndSendToUser(principal.getName(), "/topic/reply", "this shit with send to user works");
        return "hello world";
    }

    @MessageMapping("/private_message")
    @SendToUser("/topic/reply")
    public String processMessagePrivate(Principal principal, @Payload String message) throws Exception {
        System.out.println(principal.getName());
//        String name = new Gson().fromJson(message, Map.class).get("name").toString();
        System.out.println("private" + message);
//        template.convertAndSend("/topic/reply", "broadcast part");
        template.convertAndSendToUser(principal.getName(), "/topic/reply", "this shit with send to user works in private msgs");
        /////////////////////////////// tests

        if (message.equals("setPrincipal"))
        {
            System.out.println("set principal");
        }


        return "hello private world";
    }

}
