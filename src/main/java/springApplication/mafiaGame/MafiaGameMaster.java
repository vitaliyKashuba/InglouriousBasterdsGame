package springApplication.mafiaGame;

import org.springframework.stereotype.Component;
import springApplication.game.BasicGameMaster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class MafiaGameMaster extends BasicGameMaster
{

    public enum Roles
    {
        MAFIA, CITIZEN, COMISSAR, SHERIFF, MANIAK, WHORE, IMMORTAL
    }

    @Override
    public void startGame(int adminId)
    {

    }

    public static List<Roles> getAllRoles()
    {
        return Arrays.asList(Roles.values());
    }

    /**
     * return current admins room size
     */
    public int getPlayersCount(int adminId)
    {
        return getRoomByRoomId(getRoomIdByAdminId(adminId)).size();
    }

}
