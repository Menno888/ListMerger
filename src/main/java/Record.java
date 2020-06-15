import java.util.LinkedHashMap;

public class Record {
    private String Artiest;
    private String Nummer;
    private LinkedHashMap<String, Object> positionMap = new LinkedHashMap<>();

    public Record() {

    }

    public Record(String artiest, String nummer) {
        this.Artiest = artiest;
        this.Nummer = nummer;
    }

    public String showSong() {
        String result = this.Artiest + " - " + this.Nummer;
        return result;
    }

    public void setArtiest(String a) {
        this.Artiest = a;
    }

    public String getArtiest() {
        return this.Artiest;
    }

    public void setNummer(String n) {
        this.Nummer = n;
    }

    public String getNummer() {
        return this.Nummer;
    }

    public void setPositionMap(LinkedHashMap<String, Object> m) {
        this.positionMap = m;
    }

    public LinkedHashMap<String, Object> getPositionMap() {
        return positionMap;
    }

    public void cleanRecord() {
        Artiest = null;
        Nummer = null;
        positionMap.clear();
    }

    public void cleanPositionMap() {
        positionMap.clear();
    }
}
