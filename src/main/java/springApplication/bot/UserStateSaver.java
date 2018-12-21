package springApplication.bot;

import org.springframework.stereotype.Component;
import springApplication.game.EGame;

import java.util.HashMap;
import java.util.Map;

/**
 * save telegram user state
 * need to realize telegram scenarios
 */
@Component
public class UserStateSaver
{
    /**
     * need to realize bot scenarios
     * statuses, started with 'GAME_NAME' only for 'GAME_NAME' use
     */
    enum Status
    {
        NEW_PLAYER, JOINREQUEST, JOINED, READY, MAFIA_SET_ROLES
    }

    private Map<Integer, Status> statuses;
    private Map<Integer, EGame> gamers;

    UserStateSaver()
    {
        statuses = new HashMap<>();
        gamers = new HashMap<>();
    }

    public void setStatus(int playerId, Status status)
    {
        statuses.put(playerId, status);
    }

    public Status getStatus(int playerId)
    {
        if (!statuses.containsKey(playerId))
        {
            setStatus(playerId, Status.NEW_PLAYER);
        }
        return statuses.get(playerId);
    }

    public void setPlayersGame(int playerId, EGame game)
    {
        gamers.put(playerId, game);
    }

    public EGame getPlayersGame(int playerId)
    {
        return gamers.get(playerId);
    }
}
