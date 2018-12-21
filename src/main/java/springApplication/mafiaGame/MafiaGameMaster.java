package springApplication.mafiaGame;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import org.springframework.stereotype.Component;
import springApplication.game.BasicGameMaster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class MafiaGameMaster extends BasicGameMaster        // TODO add master's helper? add secret voting ?
{

    public enum Roles
    {
        MAFIA, CITIZEN, COMISSAR, SHERIFF, MANIAK, WHORE
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
