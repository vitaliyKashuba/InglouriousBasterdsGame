package bot;

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
public class IBGameMaster
{
    private static IBGameMaster ourInstance = new IBGameMaster();

    public static IBGameMaster getInstance()
    {
        return ourInstance;
    }

    private Map<Integer, List<IBPlayer>> rooms; //room id - key, list of players - value
    private Map<Integer, IBPlayer> players;     //player id - key, player obj - value
    private Map<Integer, Integer> roomCreators; //admin id - key, room id - value

    //TODO add method to remove player from room when he enter new one
    //TODO add google images search
    //TODO add more text replies

    private IBGameMaster()
    {
        rooms = new HashMap<>();
        players = new HashMap<>();
        roomCreators = new HashMap<>();
    }

    /**
     * create new room and add initiator in it
     *
     * @return room number
     */
    public int initGame(int initiatorId, String initiatorName)
    {
        int roomNumber = newRoom();

        addPlayerIfNull(initiatorId, initiatorName);
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
    private void removeOldRoomIfExist(int playerId)
    {
        if (roomCreators.containsKey(playerId))
        {
            rooms.remove(roomCreators.get(playerId));
        }
    }

    public void changeStatus(int playerId, IBPlayer.Status status)
    {
        players.get(playerId).setStatus(status);
    }

    public void enterRoom(int playerId, int roomId)
    {
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
        List players = rooms.get(room);

        return players;
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
            addPlayer(new IBPlayer(id, name));
        }
    }

    /**
     * @param roomAdminId id of room admin
     *                    needs to select room for randomizing
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

}
