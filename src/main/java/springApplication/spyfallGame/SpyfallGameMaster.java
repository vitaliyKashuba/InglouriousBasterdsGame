package springApplication.spyfallGame;

import org.springframework.stereotype.Component;
import springApplication.game.BasicGameMaster;
import springApplication.game.EGame;
import springApplication.game.Player;

import java.util.ArrayList;

@Component
public class SpyfallGameMaster extends BasicGameMaster
{
    private SpyfallGameMaster()
    {
        super();

        rooms.put(2,new ArrayList<>()); // for tests
    }
}
