import java.util.LinkedHashMap;

public class Record {
    private String artiest;
    private String nummer;
    private LinkedHashMap<String, Integer> positionMap = new LinkedHashMap<>();

    public Record() {

    }

    public Record(String artiest, String nummer) {
        this.artiest = artiest;
        this.nummer = nummer;
    }

    public String showSong() {
        return this.artiest + " - " + this.nummer;
    }

    public void setArtiest(String a) {
        this.artiest = a;
    }

    public String getArtiest() {
        return this.artiest;
    }

    public void setNummer(String n) {
        this.nummer = n;
    }

    public String getNummer() {
        return this.nummer;
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
