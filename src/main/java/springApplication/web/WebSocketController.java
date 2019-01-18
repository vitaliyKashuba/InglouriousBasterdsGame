package springApplication.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import springApplication.game.RoomsKeeper;
import springApplication.ibGame.IBGameMaster;
import springApplication.game.MessageSender;
import springApplication.spyfallGame.SpyfallGameMaster;

import java.security.Principal;


@Controller
public class WebSocketController
{
    @Autowired
    MessageSender ms;

    @Autowired
    private IBGameMaster ibGameMaster;

    @Autowired
    private SpyfallGameMaster spyfallGameMaster;

    @Autowired
    private RoomsKeeper roomsKeeper;

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/message/{id}")
    @SendTo("/topic/reply/{id}")
    public String processMessageBroadcas(Principal principal, @Payload String message, @DestinationVariable int id)
    {
//        String name = new Gson().fromJson(message, Map.class).get("name").toString();
//        System.out.println(sha);
//        System.out.println(sha.getSessionId());
//        System.out.println(sha.getSessionAttributes());
//        System.out.println(principal.getName());
//        System.out.println("/message" + message + " " + id);
        template.convertAndSendToUser(principal.getName(), "/topic/reply", "this shit with send to user works");
        return message;
    }

    @MessageMapping("/private_message")
    @SendToUser("/topic/reply")
    public String processMessagePrivate(Principal principal, @Payload String message)
    {
        if (message.startsWith("setPrincipal"))                                                                         // TODO try to refactor ?
        {
            int playerId = Integer.parseInt(message.split(":")[1]);
//            System.out.println("set principal to " + playerId);
            if (ibGameMaster.containsPlayer(playerId))
            {
                ibGameMaster.getPlayer(playerId).setWebPrincipal(principal.getName());
            } else if (spyfallGameMaster.containsPlayer(playerId))
            {
                spyfallGameMaster.getPlayer(playerId).setWebPrincipal(principal.getName());
            }
            return "principal setted!";
        }

        return "hello private world";
    }

}
