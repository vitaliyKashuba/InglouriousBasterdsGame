public class IBPlayer
{
    private int id;
    private String character;
    private Status status;

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    enum Status
    {
        NONE, JOINREQUEST, JOINED, READY, INGAME
    }

    @Override
    public String toString() {
        return "IBPlayer{" +
                "id=" + id +
                ", character='" + character + '\'' +
                ", status=" + status +
                '}';
    }
}
