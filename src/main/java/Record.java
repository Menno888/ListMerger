import java.util.LinkedHashMap;

public class Record {
    private String artist;
    private String title;
    private LinkedHashMap<String, Integer> positionMap = new LinkedHashMap<>();

    public Record() {

    }

    public Record(String artist, String title) {
        this.artist = artist;
        this.title = title;
    }

    public String showSong() {
        return this.artist + " - " + this.title;
    }

    public void setArtist(String a) {
        this.artist = a;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setTitle(String n) {
        this.title = n;
    }

    public String getTitle() {
        return this.title;
    }

    public void setPositionMap(LinkedHashMap<String, Integer> m) {
        this.positionMap = m;
    }

    public LinkedHashMap<String, Integer> getPositionMap() {
        return this.positionMap;
    }

    public void cleanPositionMap() {
        this.positionMap.clear();
    }

    public void addPositionToMap(String key, int value) {
        this.positionMap.put(key, value);
    }
}
