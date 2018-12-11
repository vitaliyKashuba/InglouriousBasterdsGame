package springApplication.game;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springApplication.ibGame.IBGameMaster;
import util.Randomizer;

import java.util.HashMap;
import java.util.Map;

/**
 * service, that store room number-game type
 *
 * to make possible connect to any game by entering only room id, without game selectiong for joining people
 */
@Component
public class RoomsKeeper
{

    @Autowired
    private IBGameMaster ibGameMaster;

    private Map<Integer, EGame> rooms;  // key - room id, value - game

    RoomsKeeper()
    {
        rooms = new HashMap<>();

        rooms.put(1, EGame.INGLORIOUS_BASTERDS);    //for tests and debug
        rooms.put(2, EGame.SPYFALL);                //for tests and debug
    }

    public int newRoom(EGame game)
    {
        int roomNumber;
        do {
            roomNumber = Randomizer.getRandomRoomNumber();
        } while (rooms.keySet().contains(roomNumber));

        rooms.put(roomNumber, game);

        return roomNumber;
    }

    public EGame getGameByRoomId(int roomId)
    {
        if (!rooms.containsKey(roomId))
        {
            throw new IllegalArgumentException("no such room");
        }
        return rooms.get(roomId);
    }
}
