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

    public enum IBStatus
    {
        NONE, JOINREQUEST, JOINED, READY
    }

    private int id;
    @Setter
    private String webPrincipal;
    private String name;
    private ClientType clientType;

    @Setter private String ibCharacter;
    @Setter private IBStatus ibStatus;

    protected Player(int id, String name, ClientType clientType)
    {
        this.id = id;
        this.name = name;
        this.clientType = clientType;

//        this.ibStatus = IBPlayer.IBStatus.NONE;
    }

    public Player(int id, String name, ClientType clientType, EGame game)
    {
        this(id, name, clientType);

        switch (game)
        {
            case INGLORIOUS_BASTERDS:
                this.ibStatus = IBStatus.NONE;
                break;
            case SPYFALL:
                //TODO add
                break;
        }
    }
}
