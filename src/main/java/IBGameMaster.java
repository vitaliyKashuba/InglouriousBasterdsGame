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

    public int initGame(int initiatorId)
    {
        int roomNumber = newRoom();

        rooms.get(roomNumber).add(players.get(initiatorId));

        return roomNumber;
    }

    public Map<Integer, IBPlayer> getPlayers()
    {
        return players;
    }

    public void addPlayer(IBPlayer p)
    {
        players.put(p.getId(), p);
    }

    //add player

    //randomize

    //return vals
}
