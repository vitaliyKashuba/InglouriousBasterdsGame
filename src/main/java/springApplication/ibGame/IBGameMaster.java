package springApplication.ibGame;

import org.springframework.stereotype.Component;
import springApplication.game.BasicGameMaster;
import springApplication.game.EGame;
import springApplication.game.Player;
import util.AppUtil;
import util.GoogleSearchAPIUtil;

import java.util.*;

/**
 *
 * room admin workflow: /init -> initGame() -> returns room id -> admin enter character ->
 *      admin wait for all players ready and type /go -> randomize roles and send broadcast
 *
 * regular player workflow: /join -> enter room number -> enter character -> wait till broadcast sent
 *
 */
@Component
public class IBGameMaster extends BasicGameMaster
{
    /**
     * classic mode - download picture of character, send to player his own character to recognize
     *
     * list - send to player list of his teammates and their characters
     */
    public enum GameMode
    {
        CLASSIC, LIST   // TODO change to more obvious enum ?
    }

    private IBGameMaster()
    {
        super();

        rooms.put(1,new ArrayList<>()); // for tests
    }

    public void setCharacter(int playerId, String character)
    {
        players.get(playerId).setIbCharacter(character);
    }

    /**
     * @param roomAdminId id of room admin
     *                    needs to select room for randomizing
     *
     *    return room id
     */
    private void randomizeCharacters(int roomAdminId)
    {
        int roomId = roomCreators.get(roomAdminId);
        List<Player> players = rooms.get(roomId);
        int lastIndex = players.size()-1;
        String firstCharacterBkp = players.get(0).getIbCharacter();
        for (int i = 0; i < lastIndex; i++)
        {
            players.get(i).setIbCharacter(players.get(i+1).getIbCharacter());
        }
        players.get(lastIndex).setIbCharacter(firstCharacterBkp);
    }

    private int getRoomIdByAdminId(int adminId)
    {
        return roomCreators.get(adminId);
    }

    public void startGame(int adminId, GameMode mode)
    {
        // TODO remove not-ready players, because they may be disconnected
        // TODO check not null character ?
        randomizeCharacters(adminId);

        int roomId = getRoomIdByAdminId(adminId);
        List<Player> players = rooms.get(roomId);

        switch (mode)
        {
            case CLASSIC:
                for (Player p : players)
                {
                    String ch = p.getIbCharacter();
                    String img = GoogleSearchAPIUtil.findImage(p.getIbCharacter());
//                  sendImageFromUrl(p.getId(), img, ch);       // TODO what should be sent to web api ?  // TODO remake to telegram bots  v 4.1
                }
//                return;
                break;
            case LIST:
                for (Player p : players)
                {
                    Map<String, String> teammates = new HashMap<>();
                    for (Player pl : players)
                    {
                        if(pl.getId() != p.getId())
                        {
                            teammates.put(pl.getName(), pl.getIbCharacter());
                        }
                    }
                    switch(p.getClientType())
                    {
                        case TELEGRAM:
                            String resp = AppUtil.convertTeammatesForTelegram(teammates);
                            messageSender.sendMesageToUser(p, resp);
                            break;
                        case WEB:
                            List<Teammate> tm = AppUtil.convertTeammatesForWebApi(teammates);
                            messageSender.sendMesageToUser(p, AppUtil.toJson(tm));
                            break;
                    }
                }
                break;
        }

        messageSender.sendBroadcast("game started!!!", roomId);
    }

    /**test for web api*/
    public List<Player> getRoom(int id)
    {
        return rooms.get(id);
    }

    /**test for web api*/
    public Collection<Player> getPlayers() {return players.values();}

}
