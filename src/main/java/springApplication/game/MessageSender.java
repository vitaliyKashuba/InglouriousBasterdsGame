package springApplication.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageSender
{
    @Autowired
    private SimpMessagingTemplate template;

    public void sendBroadcast(String message, int roomId)
    {
        template.convertAndSend("/topic/reply/" + roomId, message);
    }

}
