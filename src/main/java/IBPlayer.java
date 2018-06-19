public class IBPlayer
{
    private int id;
    private String character;
    Status status;

    public IBPlayer(int id)
    {
        this.id = id;
        this.status = Status.NONE;
    }

    public int getId() {
        return id;
    }

    public String getCharacter() {
        return character;
    }

    enum Status
    {
        NONE, INIT, JOIN, READY, INGAME
    }
}
