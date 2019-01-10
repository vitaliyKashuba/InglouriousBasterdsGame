package springApplication.game;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

/**
 * can't split to IBPlayer, SpyfallPlayer etc, because need to cast everywhere in game masters then
 */
@ToString
@Getter
public class Player
{
    public enum ClientType
    {
        TELEGRAM, WEB
    }

    private int id;
    @Setter @Nullable
    private String webPrincipal;    // only for web api players
    private String name;
    private ClientType clientType;

    @Setter @Nullable
    private String character;       // character for IB, role for mafia

    public Player(int id, String name, ClientType clientType)
    {
        this.id = id;
        this.name = name;
        this.clientType = clientType;
    }
}
