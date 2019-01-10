package springApplication.game;

import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public abstract class BasicGameMaster {

    @Autowired
    private RoomsKeeper roomsKeeper;

    @Autowired
    protected MessageSender messageSender;

    // TODO rewrite with guava ?
    protected Map<Integer, List<Player>> rooms; //room id - key, list of players - value
    protected Map<Integer, Player> players;     //player id - key, player obj - value
    protected Map<Integer, Integer> roomCreators; //admin id - key, room id - value

    protected BasicGameMaster()
    {
        rooms = new HashMap<>();
        players = new HashMap<>();
        roomCreators = new HashMap<>();
    }

    /**
     * generate unique room id
     *
     * //TODO add empty rooms removing
     */
    private int newRoom(EGame game)
    {
        int roomNumber = roomsKeeper.newRoom(game);
        rooms.put(roomNumber, new ArrayList<>());
        return roomNumber;
    }

    protected int getRoomIdByAdminId(int adminId)
    {
        return roomCreators.get(adminId);
    }

    protected List<Player> getRoomByRoomId(int roomId)
    {
        return rooms.get(roomId);
    }

    /**
     * create new room and add initiator in it
     *
     * @return room number
     */
    public int initGame(int initiatorId, String initiatorName)
    {
        int roomNumber;

        switch(this.getClass().getSimpleName())     // can't make in in extension classes
        {
            case "IBGameMaster":
                roomNumber = newRoom(EGame.INGLORIOUS_BASTERDS);
                break;
            case "SpyfallGameMaster":
                roomNumber = newRoom(EGame.SPYFALL);
                break;
            case "MafiaGameMaster":
                roomNumber = newRoom(EGame.MAFIA);
                break;
            default:
                throw new NotImplementedException("implement it!");
        }

        addPlayerIfNull(initiatorId, initiatorName);
        enterRoom(initiatorId, roomNumber);

        removeOldRoomIfExist(initiatorId);
        roomCreators.put(initiatorId, roomNumber);

        return roomNumber;
    }

    /**
     * check is player exist, create if not
     *
     * it possible if players avoid calling /start method
     */
    private void addPlayerIfNull(int id, String name)
    {
        Player p = players.get(id);
        if (p == null)
        {
            addPlayer(new Player(id, name, Player.ClientType.TELEGRAM));
        }
    }

    public void enterRoom(int playerId, int roomId)
    {
        if (!rooms.containsKey(roomId))
        {
            throw new IllegalArgumentException("no such room");
        }
        rooms.get(roomId).add(players.get(playerId));
    }

    /**
     * remove old admins room
     *
     * @param playerId id of new rood admin
     */
    private void removeOldRoomIfExist(int playerId)
    {
        if (roomCreators.containsKey(playerId))
        {
            rooms.remove(roomCreators.get(playerId));
            roomCreators.remove(playerId);
        }
    }

    // TODO private?
    public void addPlayer(@NotNull  Player p)
    {
        players.put(p.getId(), p);
    }

    public Player getPlayer(int id)
    {
        return players.get(id);
    }

    public boolean isAdmin(int id)
    {
        return roomCreators.containsKey(id);
    }

    public void join(int playerId, String playerName, int roomId)
    {
        addPlayerIfNull(playerId, playerName);
        removeOldRoomIfExist(playerId);
        enterRoom(playerId, roomId);
    }

    /**
     * start game for admin's room
     */
    public abstract void startGame(int adminId);
}
