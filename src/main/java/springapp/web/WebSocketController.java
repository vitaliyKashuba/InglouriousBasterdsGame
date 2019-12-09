package springapp.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import springapp.ibGame.IBGameMaster;
import springapp.game.MessageSender;
import springapp.mafiaGame.MafiaGameMaster;
import springapp.spyfallGame.SpyfallGameMaster;

import java.security.Principal;

@Slf4j
@Controller
public class WebSocketController {
    @Autowired
    MessageSender ms;

    @Autowired
    private IBGameMaster ibGameMaster;

    @Autowired
    private SpyfallGameMaster spyfallGameMaster;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private MafiaGameMaster mafiaGameMaster;

    @MessageMapping("/message/{id}")
    @SendTo("/topic/reply/{id}")
    public String processMessageBroadcas(Principal principal, @Payload String message, @DestinationVariable int id) {
        template.convertAndSendToUser(principal.getName(), "/topic/reply", "this shit with send to user works");
        return message;
    }

    @MessageMapping("/private_message")
    @SendToUser("/topic/reply")
    public String processMessagePrivate(Principal principal, @Payload String message) {
        if (message.startsWith("setPrincipal"))                                                                         // TODO try to refactor ?
        {
            int playerId = Integer.parseInt(message.split(":")[1]);
            if (ibGameMaster.containsPlayer(playerId)) {
                ibGameMaster.getPlayer(playerId).setWebPrincipal(principal.getName());
            } else if (spyfallGameMaster.containsPlayer(playerId)) {
                spyfallGameMaster.getPlayer(playerId).setWebPrincipal(principal.getName());
            } else if (mafiaGameMaster.containsPlayer(playerId)) {
                mafiaGameMaster.getPlayer(playerId).setWebPrincipal(principal.getName());
            } else {
                log.error("smth going wrong while setting up principal to player");
                return "error";
            }
            return "principal setted!";
        }

        return "hello private world";
    }

}
