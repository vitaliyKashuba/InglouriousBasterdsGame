package springApplication.game;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public abstract class BasicGameMaster {

    @Autowired
    private RoomsKeeper roomsKeeper;

    @Autowired
    protected MessageSender messageSender;

    @Autowired
    protected LobbyMaster lobbyMaster;

    protected Map<Integer, List<Player>> rooms; //room id - key, list of players - value
    protected Map<Integer, Player> players;     //player id - key, player obj - value
    protected BiMap<Integer, Integer> roomCreators; //admin id - key, room id - value

    protected BasicGameMaster()
    {
        rooms = new HashMap<>();
        players = new HashMap<>();
        roomCreators = HashBiMap.create();
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

    protected int getAdminIdByRoomId(int roomId)
    {
        return roomCreators.inverse().get(roomId);
    }

    /** looks like shit, but works!
     *
     * @throws IllegalArgumentException dont have idea in what conditions it can be thrown, but IDE says it possible
     */
    protected int gerRoomNumberByPlayerId(int id)
    {
        return rooms.keySet().stream()
                .filter(room -> rooms.get(room)
                        .stream()
                        .filter(player -> player.getId() == id)
                        .count() == 1)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("cant find room"));
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

        lobbyMaster.initLobby(initiatorId, roomNumber);

        return roomNumber;
    }

    /**
     * check is player exist, create if not
     *
     * it possible if players avoid calling /start method
     */
    private void addPlayerIfNull(int id, String name, Player.ClientType clientType)
    {
        Player p = players.get(id);
        if (p == null)
        {
            addPlayer(new Player(id, name, clientType));
        }
    }

    /** old signature, used only in telegram, so left with default client type param = TELEGRAM */
    private void addPlayerIfNull(int id, String name)
    {
        addPlayerIfNull(id, name, Player.ClientType.TELEGRAM);
    }

    private void enterRoom(int playerId, int roomId)
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
    private void addPlayer(@NotNull  Player p)
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

    /** old signature, used only in telegram, so left with default client type param = TELEGRAM */
    public void join(int playerId, String playerName, int roomId)
    {
        join(playerId, playerName, roomId, Player.ClientType.TELEGRAM);
    }

    public void join(int playerId, String playerName, int roomId, Player.ClientType clientType)
    {
        addPlayerIfNull(playerId, playerName, clientType);
        removeOldRoomIfExist(playerId);
        enterRoom(playerId, roomId);

        updateLobby(roomId);
    }

    /**
     * updates lobby
     * override to customize message for each game
     */
    protected void updateLobby(int roomId)
    {
        try //TODO hotfixed, find out and fix cause of null pointer
        {
            lobbyMaster.updateLobby(getAdminIdByRoomId(roomId), roomId, "Joined " + getRoomByRoomId(roomId).size());
        } catch (Exception e)
        {
            log.error("Error while try to update lobby", e);
            e.printStackTrace();
        }
    }

    /**
     * start game for admin's room
     */
    public abstract void startGame(int adminId);

    public boolean containsPlayer(int id)
    {
        return this.players.containsKey(id);
    }
}
