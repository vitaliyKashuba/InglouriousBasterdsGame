package springApplication.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import springApplication.bot.IngloriousBastardBot;

@Service
public class MessageSender
{
    @Autowired
    private IngloriousBastardBot bot;

    @Autowired
    private SimpMessagingTemplate template;

    public void sendBroadcast(String message, int roomId)
    {
        template.convertAndSend("/topic/reply/" + roomId, message);
    }

    public void sendMesageToUser(IBPlayer reciever, String message)
    {
        switch(reciever.getClientType())
        {
            case TELEGRAM:
                bot.sendMsg(reciever.getId(), message);
                break;
            case WEB:
//                template.convertAndSend("/topic/reply/" + roomId, message);
//                template.convertAndSendToUser();
                // TODO send to web socket user here
                template.convertAndSendToUser(reciever.getWebPrincipal(), "/topic/reply", message);
                break;
        }
    }

}
