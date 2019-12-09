package springapp.game;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import springapp.bot.IngloriousBastardBot;

/**
 * used to send messages to players without holding on their client
 */
@Slf4j
@Service
public class MessageSender {
    @Autowired
    private IngloriousBastardBot bot;

    @Autowired
    private SimpMessagingTemplate template;

    /**
     * send broadcast message to web api client players
     */
    public void sendBroadcast(String message, int roomId) {
        template.convertAndSend("/topic/reply/" + roomId, message);
    }

    public void sendMesageToUser(Player reciever, String message) {
        switch (reciever.getClientType()) {
            case TELEGRAM:
                bot.sendMsg(reciever.getId(), message);
                break;
            case WEB:
                template.convertAndSendToUser(reciever.getWebPrincipal(), "/topic/reply", message);
                break;
            default:
                log.error("Default case in sendMessageToUser() !");
        }
    }

    public void sendImageFromUrl(Player reciever, String imgUrl, String msg) {
        switch (reciever.getClientType()) {
            case TELEGRAM:
                bot.sendImageFromUrl(reciever.getId(), imgUrl, msg);
                break;
            case WEB:
                break;//TODO should be sent to web users?
        }
    }

}
