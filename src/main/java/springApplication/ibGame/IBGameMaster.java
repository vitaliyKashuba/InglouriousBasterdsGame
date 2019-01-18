package springApplication.ibGame;

import org.springframework.stereotype.Component;
import springApplication.game.BasicGameMaster;
import springApplication.game.Player;
import util.Convertor;
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
        players.get(playerId).setCharacter(character);
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
        String firstCharacterBkp = players.get(0).getCharacter();
        for (int i = 0; i < lastIndex; i++)
        {
            players.get(i).setCharacter(players.get(i+1).getCharacter());
        }
        players.get(lastIndex).setCharacter(firstCharacterBkp);
    }

    /**
     * @return id of current room created by user
     */
    public int getAdminRoomId(int adminId)
    {
        return getRoomIdByAdminId(adminId);
    }

    @Override
    public void startGame(int adminId)  // probably should never call
    {
        startGame(adminId, GameMode.LIST);
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
                    String ch = p.getCharacter();
                    String img = GoogleSearchAPIUtil.findImage(p.getCharacter());
                    messageSender.sendImageFromUrl(p, img, ch);
                }
                break;
            case LIST:
                for (Player p : players)
                {
                    Map<String, String> teammates = new HashMap<>();
                    for (Player pl : players)
                    {
                        if(pl.getId() != p.getId())
                        {
                            teammates.put(pl.getName(), pl.getCharacter());
                        }
                    }
                    switch(p.getClientType())
                    {
                        case TELEGRAM:
                            String resp = Convertor.convertTeammatesForTelegram(teammates);
                            messageSender.sendMesageToUser(p, resp);
                            break;
                        case WEB:
                            List<Teammate> tm = Convertor.convertTeammatesForWebApi(teammates);
                            messageSender.sendMesageToUser(p, "teammates" + Convertor.toJson(tm));
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
