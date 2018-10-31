package springApplication.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageSender
{
    @Autowired
    private SimpMessagingTemplate template;

//    private static MessageSender ourInstance = new MessageSender();

    public MessageSender() {
        System.out.println("constructed");
    }

//    public static MessageSender getInstance()
//    {
//        return ourInstance;
//    }

    public void sendBroadcast(String message, int roomId)
    {
        template.convertAndSend("/topic/reply/" + roomId, message);
    }

}
