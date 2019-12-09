package springapp.game;

import org.springframework.stereotype.Component;
import util.Randomizer;

import java.util.HashMap;
import java.util.Map;

/**
 * service, that store room number-game type
 * <p>
 * to make possible connect to any game by entering only room id, without game selectiong for joining people
 */
@Component
public class RoomsKeeper {
    private Map<Integer, EGame> rooms;  // key - room id, value - game

    RoomsKeeper() {
        rooms = new HashMap<>();

        rooms.put(1, EGame.INGLORIOUS_BASTERDS);    //for tests and debug
        rooms.put(2, EGame.SPYFALL);                //for tests and debug
    }

    /**
     * NOT creating room, but only keep room id and game type of this room
     * <p>
     * room creation is on GameMasters components
     */
    public int newRoom(EGame game) {
        int roomNumber;
        do {
            roomNumber = Randomizer.getRandomRoomNumber();
        } while (rooms.containsKey(roomNumber));

        rooms.put(roomNumber, game);

        return roomNumber;
    }

    public EGame getGameByRoomId(int roomId) {
        if (!rooms.containsKey(roomId)) {
            throw new IllegalArgumentException("no such room");
        }
        return rooms.get(roomId);
    }
}
