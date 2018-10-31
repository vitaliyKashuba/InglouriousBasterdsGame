package springApplication.game;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
public class IBPlayer
{
    private int id;
    @Setter private String character;
    @Setter private Status status;
    private String name;

    public IBPlayer(int id, String name)
    {
        this.id = id;
        this.status = Status.NONE;
        this.name = name;
    }

    public enum Status
    {
        NONE, JOINREQUEST, JOINED, READY, INGAME
    }
}
