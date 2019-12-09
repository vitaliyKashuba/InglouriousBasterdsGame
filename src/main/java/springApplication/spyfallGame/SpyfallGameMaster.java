package springApplication.spyfallGame;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import springApplication.game.BasicGameMaster;
import springApplication.game.Player;
import util.AppUtil;
import util.Convertor;
import util.Randomizer;

import java.io.*;
import java.util.*;

@Slf4j
@Component
public class SpyfallGameMaster extends BasicGameMaster
{
    private Map<String, List<String>> locationsAndRoles;    // key - location, value - roles
    private Map<Integer, Integer> roomLocationsLimit;       // key - room id, value - limit
    private final static int MINIMUM_LOCATIONS_LIMIT = 5;

    private SpyfallGameMaster()
    {
        super();

        roomLocationsLimit = new HashMap<>();

        rooms.put(2,new ArrayList<>()); // for tests

        try
        {
            String s = AppUtil.readFromResources("spyfall.json");                                          // load locations and roles from resources

            ObjectMapper mapper = new ObjectMapper();
            locationsAndRoles = mapper.readValue(s, Map.class);
        } catch (FileNotFoundException e)
        {
            log.error("smth wrong with file"); //TODO add some error message if can't init ?
            log.info("classpath:");
            AppUtil.printClasspath();
            e.printStackTrace();
        } catch (IOException e)
        {
            log.error("probably Jackson error");
            e.printStackTrace();
        }
    }

    @Override
    public void startGame(int adminId) {
        int roomId = getRoomIdByAdminId(adminId);
        List<Player> players = rooms.get(roomId);

        Player spy = Randomizer.getRandomElement(players);                                                              // select spy

        List<String> locations = new ArrayList<>(locationsAndRoles.keySet());

        String location;
        if (roomLocationsLimit.containsKey(roomId))                                                                     // select random location with limit
        {
            Collections.shuffle(locations);
            locations = locations.subList(0, roomLocationsLimit.get(roomId));
            location = Randomizer.getRandomElement(locations);
        } else
        {
            location = Randomizer.getRandomElement(locations);
        }

        List<String> rls = locationsAndRoles.get(location);                                                             // get location roles
        Collections.shuffle(rls);
        Stack<String> roles = new Stack<>();
        roles.addAll(rls);

        for (Player p : players)    // TODO send img with location ?
        {
            StringBuilder sbRoleMessage = new StringBuilder();

            if (p.equals(spy))                                                                                          // build game data response (role/location)
            {
                sbRoleMessage.append("SPY");
            } else
            {
                sbRoleMessage.append("Location: ").append(location);

                if (!roles.empty())
                {
                    sbRoleMessage.append("\nRole: ").append(roles.pop());
                }
            }

            String locationsMessage;
            String roleMessage;
            switch(p.getClientType())                                                                                   // convert responses to web/tg format // TODO move conversion logic to another component ?
            {
                case TELEGRAM:
                    locationsMessage = Convertor.convertLocationsForTelegram(locations);
                    roleMessage = sbRoleMessage.toString();
                    break;
                case WEB:
                    locationsMessage = "spyfallLocations" + Convertor.toJson(locations);
                    roleMessage = "spyfallRole" + sbRoleMessage.toString();
                    break;
                default:
                    log.error("default switch while getting client type in spyfall start " + p.getClientType());
                    return;
            }

            messageSender.sendMesageToUser(p, locationsMessage);                                                        // send locations list
            messageSender.sendMesageToUser(p, roleMessage);                                                             // send role
        }
    }

    public void setLocationsLimit(int adminId, int limit)
    {
        if(limit < MINIMUM_LOCATIONS_LIMIT)
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

    /** for test*/
    public List<String> getAllLocations()
    {
        return new ArrayList<>(locationsAndRoles.keySet());
    }
}
