import java.util.*;

public class IBGameMaster
{
    private static IBGameMaster ourInstance = new IBGameMaster();

    public static IBGameMaster getInstance()
    {
        return ourInstance;
    }

    private IBGameMaster()
    {
        rooms = new HashMap<Integer, List<IBPlayer>>();
        players = new HashMap<>();
    }

    private Map<Integer, List<IBPlayer>> rooms;
    private Map<Integer, IBPlayer> players;

    public int initGame(int initiatorId)
    {
        int roomNumber = newRoom();

        addPlayerIfNull(initiatorId);
        enterRoom(initiatorId, roomNumber);

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

    private int newRoom()
    {
        int roomNumber;
        do {
            roomNumber = Randomizer.getRandomRoomNumber();
        } while (rooms.keySet().contains(roomNumber));

        rooms.put(roomNumber, new ArrayList<IBPlayer>());

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

}
