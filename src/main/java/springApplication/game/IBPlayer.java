package springApplication.game;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
public class IBPlayer
{
    public enum Status
    {
        NONE, JOINREQUEST, JOINED, READY
    }

    public enum ClientType
    {
        TELEGRAM, WEB
    }

    private int id;
    @Setter private String character;
    @Setter private Status status;
    @Setter private String webPrincipal;
    private String name;
    private ClientType clientType;

    public IBPlayer(int id, String name, ClientType clientType)
    {
        this.id = id;
        this.status = Status.NONE;
        this.name = name;
        this.clientType = clientType;
    }
}
