package springApplication.bot;

import org.springframework.stereotype.Component;
import springApplication.game.EGame;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserStateSaver
{
    enum Status
    {
        NEW_PLAYER, JOINREQUEST, JOINED, READY
    }

    private Map<Integer, Status> statuses;
    private Map<Integer, EGame> gamers; // TODO move this somwhere else ?

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
