package springApplication.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.AppUtil;
import util.GoogleSearchAPIUtil;
import util.Randomizer;

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
public class IBGameMaster
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

    @Autowired
    private MessageSender messageSender;

    private Map<Integer, List<IBPlayer>> rooms; //room id - key, list of players - value
    private Map<Integer, IBPlayer> players;     //player id - key, player obj - value
    private Map<Integer, Integer> roomCreators; //admin id - key, room id - value

    private IBGameMaster()
    {
//        messageSender = MessageSender.getInstance();

        rooms = new HashMap<>();
        players = new HashMap<>();
        roomCreators = new HashMap<>();

        rooms.put(1,new ArrayList<>()); // for tests
    }

    /**
     * create new room and add initiator in it
     *
     * @return room number
     */
    public int initGame(int initiatorId, String initiatorName)
    {
        int roomNumber = newRoom();

        addPlayerIfNull(initiatorId, initiatorName);          // TODO check if possible to init game with player is null ? remove useless addIfNull method call
        enterRoom(initiatorId, roomNumber);

        removeOldRoomIfExist(initiatorId);
        roomCreators.put(initiatorId, roomNumber);

        return roomNumber;
    }

    /**
     * remove old admins room
     *
     * @param playerId id of new rood admin
     */
    public void removeOldRoomIfExist(int playerId)
    {
        if (roomCreators.containsKey(playerId))
        {
            rooms.remove(roomCreators.get(playerId));
            roomCreators.remove(playerId);
        }
    }

    public void changeStatus(int playerId, IBPlayer.Status status)
    {
        players.get(playerId).setStatus(status);
    }

    public void enterRoom(int playerId, int roomId)
    {
        if (!rooms.containsKey(roomId))
        {
            throw new IllegalArgumentException("no such room");
        }
        rooms.get(roomId).add(players.get(playerId));
    }

    public void setCharacter(int playerId, String character)
    {
        players.get(playerId).setCharacter(character);
    }

    public void addPlayer(IBPlayer p)
    {
        players.put(p.getId(), p);
    }

    public IBPlayer getPlayer(int id)
    {
        return players.get(id);
    }

    /**
     * returns list of players in room by creator request
     * used in broadcast messages
     *
     * @see /go in commands parsing
     */
    public List<IBPlayer> getPlayersByRoomCreator(int id)
    {
        int room = roomCreators.get(id);

        return rooms.get(room);
    }

    public boolean isAdmin(int id)
    {
        return roomCreators.containsKey(id);
    }

    /**
     * @return id of current room created by user
     */
    public int getAdminRoomId(int adminId)
    {
        return roomCreators.get(adminId);
    }

    /**
     * generate unique room id
     *
     * //TODO add empty rooms removing
     */
    private int newRoom()
    {
        int roomNumber;
        do {
            roomNumber = Randomizer.getRandomRoomNumber();
        } while (rooms.keySet().contains(roomNumber));

        rooms.put(roomNumber, new ArrayList<>());

        System.out.println("new room " + roomNumber);
        return roomNumber;
    }

    /**
     * check is player exist, create if not
     *
     * it possible if players avoid calling /start method
     */
    public void addPlayerIfNull(int id, String name)
    {
        IBPlayer p = players.get(id);
        if (p == null)
        {
            addPlayer(new IBPlayer(id, name, IBPlayer.ClientType.TELEGRAM));                                            // TODO fix possible bug if this ever called from web api
        }
    }

    /**
     * @param roomAdminId id of room admin
     *                    needs to select room for randomizing
     *
     *    return room id
     */
    public void randomizeCharacters(int roomAdminId)
    {
        int roomId = roomCreators.get(roomAdminId);
        List<IBPlayer> players = rooms.get(roomId);
        int lastIndex = players.size()-1;
        String firstCharacterBkp = players.get(0).getCharacter();
        for (int i = 0; i < lastIndex; i++)
        {
            players.get(i).setCharacter(players.get(i+1).getCharacter());
        }
        players.get(lastIndex).setCharacter(firstCharacterBkp);
    }

    public int getRoomIdByAdminId(int adminId)
    {
        return roomCreators.get(adminId);
    }

    public void startGame(int adminId, GameMode mode)
    {
        // TODO remove not-ready players, because they may be disconnected
        // TODO check not null character ?
        randomizeCharacters(adminId);

        int roomId = getRoomIdByAdminId(adminId);
        List<IBPlayer> players = rooms.get(roomId);

        switch (mode)
        {
            case CLASSIC:
                for (IBPlayer p : players)
                {
                    String ch = p.getCharacter();
                    String img = GoogleSearchAPIUtil.findImage(p.getCharacter());
//                  sendImageFromUrl(p.getId(), img, ch);       // TODO what should be sent to web api ?  // TODO remake to telegram bots  v 4.1
                }
//                return;
                break;
            case LIST:
                for (IBPlayer p : players)
                {
                    Map<String, String> teammates = new HashMap<>();
                    for (IBPlayer pl : players)
                    {
                        if(pl.getId() != p.getId())
                        {
                            teammates.put(pl.getName(), pl.getCharacter());
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
    public List<IBPlayer> getRoom(int id)
    {
        return rooms.get(id);
    }

    /**test for web api*/
    public Collection<IBPlayer> getPlayers() {return players.values();}

}
