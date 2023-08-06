import java.util.LinkedHashMap;

public class Record {
    private String artist;
    private String title;
    private LinkedHashMap<String, Integer> positionMap = new LinkedHashMap<>();
    private LinkedHashMap<String, Object> additionalInformationMap = new LinkedHashMap<>();

    public Record() {

    }

    public Record(String artist, String title) {
        this.artist = artist;
        this.title = title;
    }

    public String showSong() {
        return this.artist + " - " + this.title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setPositionMap(LinkedHashMap<String, Integer> positionMap) {
        this.positionMap = positionMap;
    }

    public LinkedHashMap<String, Integer> getPositionMap() {
        return this.positionMap;
    }

    public void setAdditionalInformationMap(LinkedHashMap<String, Object> additionalInformationMap) {
        this.additionalInformationMap = additionalInformationMap;
    }

    public LinkedHashMap<String, Object> getAdditionalInformationMap() {
        return this.additionalInformationMap;
    }

    public void cleanPositionMap() {
        this.positionMap.clear();
    }

    public void cleanAdditionalInformationMap() {
        this.additionalInformationMap.clear();
    }

    public void addPositionToMap(String key, int value) {
        this.positionMap.put(key, value);
    }

    public void addInfoToMap(String key, Object value) {
        this.additionalInformationMap.put(key, value);
    }
}
