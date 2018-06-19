import java.util.*;

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

    private IBGameMaster()
    {
        rooms = new HashMap<>();
        players = new HashMap<>();
        roomCreators = new HashMap<>();
    }

    public int initGame(int initiatorId)
    {
        int roomNumber = newRoom();

        addPlayerIfNull(initiatorId);
        enterRoom(initiatorId, roomNumber);
        roomCreators.put(initiatorId, roomNumber);

        return roomNumber;
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

    public void addPlayerIfNull(int id)
    {
        IBPlayer p = players.get(id);
        if (p == null)
        {
            addPlayer(new IBPlayer(id));
        }
    }

    public void randomizeCharacters(int roomAdminId)
    {
        int roomId = roomCreators.get(roomAdminId);
        List<IBPlayer> players = rooms.get(roomId);
        int lastIndex = players.size()-1;
        String firstCharacterBkp = new String(players.get(0).getCharacter());
        for (int i = 0; i < lastIndex; i++)
        {
            players.get(i).setCharacter(players.get(i+1).getCharacter());
        }
        players.get(lastIndex).setCharacter(firstCharacterBkp);
    }

}
