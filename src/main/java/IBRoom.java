import java.util.ArrayList;
import java.util.List;

public class IBRoom
{
    List<IBPlayer> players;

    public IBRoom()
    {
        players = new ArrayList<IBPlayer>();
    }

    public void addPlayer(IBPlayer p)
    {
        players.add(p);
    }

}
