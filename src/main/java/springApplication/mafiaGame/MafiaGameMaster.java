package springApplication.mafiaGame;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import org.springframework.stereotype.Component;
import springApplication.game.BasicGameMaster;
import springApplication.game.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class MafiaGameMaster extends BasicGameMaster        // TODO add master's helper? add secret voting ?
{

    public enum Roles
    {
        MAFIA, CITIZEN, COMMISSAR, SHERIFF, MANIAC, WHORE
    }

    private ListMultimap<Integer, String> roomsAndRoles;    // key - room id, values - roles. String, but not enums because of user defined roles and using of IB methods with string characters

    MafiaGameMaster()
    {
        super();

        roomsAndRoles = MultimapBuilder.hashKeys().arrayListValues().build();
    }

    public static List<Roles> getAllRoles()
    {
        return Arrays.asList(Roles.values());
    }

    @Override
    public void startGame(int adminId)
    {
        int roomId = getRoomIdByAdminId(adminId);

        List<Player> players = rooms.get(roomId);
        List<String> roles = roomsAndRoles.get(roomId);

        players.remove(0);  // firs element - admin(master), no need to set character to master

        if (roles.size() <= players.size())
        {
            int i = 0;
            for(; i < roles.size(); i++)
            {
                players.get(i).setCharacter(roles.get(i));
            }

            while(i < players.size())
            {
                players.get(i).setCharacter(Roles.CITIZEN.name());
                i++;
            }
        } else
        {
            System.out.println("too much roles");
            // TODO send error, too much roles
        }
        // randomize roles
        // send roles
        // send all roles to admin (master)
    }

    /**
     * return current admins room size
     */
    public int getPlayersCount(int adminId)
    {
        return getRoomByRoomId(getRoomIdByAdminId(adminId)).size();
    }

    /**
     *  add role to admin's room
     */
    public void addRole(int adminId, String role)
    {
        roomsAndRoles.put(getRoomIdByAdminId(adminId), role);
    }

    /**
     *  return roles, added to admins room
     */
    public List<String> getRoles(int adminId)
    {
        return roomsAndRoles.get(getRoomIdByAdminId(adminId));
    }

}
