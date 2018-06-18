public class IBPlayer
{
    private String id;
    private String character;

    public IBPlayer(String id, String character) {
        this.id = id;
        this.character = character;
    }

    public String getId() {
        return id;
    }

    public String getCharacter() {
        return character;
    }
}
