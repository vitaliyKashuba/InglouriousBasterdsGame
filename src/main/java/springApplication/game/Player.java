package springApplication.game;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
public class Player
{
    public enum ClientType
    {
        TELEGRAM, WEB
    }

    private int id;
    @Setter
    private String webPrincipal;
    private String name;
    private ClientType clientType;

    @Setter private String ibCharacter;

    public Player(int id, String name, ClientType clientType)
    {
        this.id = id;
        this.name = name;
        this.clientType = clientType;
    }
}
