package springApplication.spyfallGame;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import springApplication.game.BasicGameMaster;
import springApplication.game.Player;
import util.AppUtil;
import util.Randomizer;

import java.io.*;
import java.util.*;

@Component
public class SpyfallGameMaster extends BasicGameMaster
{
    private Map<String, List<String>> locationsAndRoles;   // key - location,
    private SpyfallGameMaster()
    {
        super();

        rooms.put(2,new ArrayList<>()); // for tests

        try {
            File file = AppUtil.loadFileFromResources("spyfall.json");

            InputStream resourceInputStream = new FileInputStream(file);
            ObjectMapper mapper = new ObjectMapper();
            locationsAndRoles = mapper.readValue(resourceInputStream, Map.class);
        } catch (FileNotFoundException e) {
            System.out.println("smth wrong with file"); //TODO add some error message if can't init ?
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Jackson error");
            e.printStackTrace();
        }
    }

    @Override
    public void startGame(int adminId) {
        int roomId = getRoomIdByAdminId(adminId);
        List<Player> players = rooms.get(roomId);

        Player spy = Randomizer.getRandomElement(players);

        String location = Randomizer.getRandomElement(locationsAndRoles.keySet());

        List<String> rls = locationsAndRoles.get(location);
        Randomizer.shufle(rls);
        Stack<String> roles = new Stack<>();
        roles.addAll(rls);

        for (Player p : players)    // TODO send img with location ?
        {
            messageSender.sendMesageToUser(p, AppUtil.toJson(locationsAndRoles.keySet()));
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

}
