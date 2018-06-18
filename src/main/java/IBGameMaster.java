import java.util.HashMap;
import java.util.Map;

public class IBGameMaster
{
    private static IBGameMaster ourInstance = new IBGameMaster();

    public static IBGameMaster getInstance()
    {
        return ourInstance;
    }

    private IBGameMaster()
    {
        rooms = new HashMap();
    }

    private Map rooms;

    //new room

    //add player

    //randomize

    //return vals
}
