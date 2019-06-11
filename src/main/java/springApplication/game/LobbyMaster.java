package springApplication.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springApplication.bot.IngloriousBastardBot;

import java.util.HashMap;
import java.util.Map;

/**
 * provides lobby message for tg room admin
 * no web client support now
 */
@Component
public class LobbyMaster
{
    @Autowired
    private IngloriousBastardBot bot;

    private Map<Integer, Integer> lobbyMessageIds; // key - room id, value - messageId

    private final String LOBBY_HEADER = "----- WAITING FOR PARTY -----";

    LobbyMaster()
    {
        lobbyMessageIds = new HashMap<>();
    }

    private void storeLobbyMessageId(int roomId, int messageId)
    {
        lobbyMessageIds.put(roomId, messageId);
    }

    private int getLobbyMessageId(int roomId)
    {
        return lobbyMessageIds.get(roomId);
    }

    /**
     *  can be used only with TG users
     */
    public void initLobby(int userId, int roomId)
    {
        int lobbyMessageId = bot.sendMsg(userId, LOBBY_HEADER);
        storeLobbyMessageId(roomId, lobbyMessageId);
    }

    /**
     * updates telegram lobby message
     */
    public void updateLobby(int userId, int roomId, String lobbyMessage)
    {
        bot.editMessage(userId, getLobbyMessageId(roomId), LOBBY_HEADER + "\n" + lobbyMessage);
    }

}
