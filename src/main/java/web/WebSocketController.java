package web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
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
        System.out.println("autowiring");
        this.template = template;
    }

    @MessageMapping("/send/message")
    public void onReceivedMesage(String message){
        System.out.println("onRecievedMesage");
        this.template.convertAndSend("/chat",  new SimpleDateFormat("HH:mm:ss").format(new Date())+"- "+message);

//        this.template.convertAndSend("12345");
    }

//    @MessageMapping("/send/message")
//    @SendToUser("/queue/reply")
//    public String processMessageFromClient( @Payload String message,Principal principal) throws Exception
//    {
//        System.out.println(message);
//        System.out.println(principal);
//        return "123";
//    }

}
