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
        rooms = new HashMap();
    }

    private Map<Integer, List> rooms;

    public int newRoom()
    {
        int roomNumber;
        do {
            roomNumber = Randomizer.getRandomRoomNumber();
        } while (rooms.keySet().contains(roomNumber));

        rooms.put(roomNumber, new ArrayList());

        System.out.println("new room " + roomNumber);
        return roomNumber;
    }

    //add player

    //randomize

    //return vals
}
