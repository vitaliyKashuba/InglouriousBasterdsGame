package springApplication.spyfallGame;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import springApplication.game.BasicGameMaster;
import springApplication.game.Player;
import util.AppUtil;
import util.Convertor;
import util.Randomizer;

import java.io.*;
import java.util.*;

@Component
public class SpyfallGameMaster extends BasicGameMaster
{
    private Map<String, List<String>> locationsAndRoles;    // key - location, value - roles
    private Map<Integer, Integer> roomLocationsLimit;       // key - room id, value - limit

    private SpyfallGameMaster()
    {
        super();

        roomLocationsLimit = new HashMap<>();

        rooms.put(2,new ArrayList<>()); // for tests

        try
        {
            String s = AppUtil.readFromResources("spyfall.json");

            ObjectMapper mapper = new ObjectMapper();
            locationsAndRoles = mapper.readValue(s, Map.class);
        } catch (FileNotFoundException e)
        {
            System.out.println("smth wrong with file"); //TODO add some error message if can't init ?
            System.out.println("classpath:");
            AppUtil.printClasspath();
            e.printStackTrace();
        } catch (IOException e)
        {
            System.out.println("probably Jackson error");
            e.printStackTrace();
        }
    }

    @Override
    public void startGame(int adminId) {
        int roomId = getRoomIdByAdminId(adminId);
        List<Player> players = rooms.get(roomId);

        Player spy = Randomizer.getRandomElement(players);

        List<String> locations = new ArrayList<>(locationsAndRoles.keySet());

        String location;
        if (roomLocationsLimit.containsKey(roomId))
        {
            Collections.shuffle(locations);
            List<String> l = new ArrayList<>(locations);
            locations = l.subList(0, roomLocationsLimit.get(roomId));
            location = Randomizer.getRandomElement(l);
        } else
        {
            location = Randomizer.getRandomElement(locations);
        }

        List<String> rls = locationsAndRoles.get(location);
        Collections.shuffle(rls);
        Stack<String> roles = new Stack<>();
        roles.addAll(rls);

        for (Player p : players)    // TODO send img with location ?
        {
            messageSender.sendMesageToUser(p, Convertor.convertLocationsForTelegram(locations));
            if (p.equals(spy))
            {
                messageSender.sendMesageToUser(p, "SPY");
            } else
            {
                messageSender.sendMesageToUser(p, "Location:" + location);

                if (!roles.empty())
                {
                    messageSender.sendMesageToUser(p, "Role:" + roles.pop());
                }
            }
        }
    }

    public void setLocationsLimit(int adminId, int limit)
    {
        if(limit < 5)
        {
            throw new IllegalArgumentException("too small limit");
        }

        if(limit > locationsAndRoles.keySet().size())
        {
            limit = locationsAndRoles.keySet().size();
        }

        int roomId = getRoomIdByAdminId(adminId);
        roomLocationsLimit.put(roomId, limit);
    }
}
