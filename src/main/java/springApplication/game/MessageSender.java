package springApplication.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import springApplication.bot.IngloriousBastardBot;

/**
 * used to send messages to players without holding on their client
 */
@Service
public class MessageSender
{
    @Autowired
    private IngloriousBastardBot bot;

    @Autowired
    private SimpMessagingTemplate template;

    /**
     * send broadcast message to web api client players
     */
    public void sendBroadcast(String message, int roomId)
    {
        template.convertAndSend("/topic/reply/" + roomId, message);
    }

    public void sendMesageToUser(Player reciever, String message)
    {
        switch(reciever.getClientType())
        {
            case TELEGRAM:
                bot.sendMsg(reciever.getId(), message);
                break;
            case WEB:
                template.convertAndSendToUser(reciever.getWebPrincipal(), "/topic/reply", message);
                break;
        }
    }

    /**
     *  can be used only with TG users, so separated from sendMessageToUser
     */
    public int sendLobbyMessage(int userId, String message) // TODO move somewhere ?
    {
        return bot.sendMsg(userId, message);
    }

    public void updateLobbyMessage(int chatId, int messageId, String message)   // TODO move somhere ?
    {
        bot.editMessage(chatId, messageId, message, null);
    }

    public void sendImageFromUrl(Player reciever, String imgUrl, String msg)
    {
        switch(reciever.getClientType())
        {
            case TELEGRAM:
                bot.sendImageFromUrl(reciever.getId(), imgUrl, msg);
                break;
            case WEB:
                break;//TODO should be sent to web users?
        }
    }

}
