package springApplication.game;

import org.springframework.stereotype.Component;
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

    private Map<Integer, EGame> rooms;

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
}
