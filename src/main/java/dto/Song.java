package dto;

import java.util.LinkedHashMap;
import java.util.Map;

import static tools.FieldUtil.SEPARATOR_CHARACTER_HYPHEN_SPACED;

public class Song {
    private String artist;
    private String title;
    private final Map<String, Integer> positionMap = new LinkedHashMap<>();
    private final Map<String, Object> additionalInformationMap = new LinkedHashMap<>();

    public Song() {
        //No-args
    }

    public Song(String artist, String title) {
        this.artist = artist;
        this.title = title;
    }

    public String showSong() {
        return this.artist + SEPARATOR_CHARACTER_HYPHEN_SPACED + this.title;
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

    public Map<String, Integer> getPositionMap() {
        return this.positionMap;
    }

    public Map<String, Object> getAdditionalInformationMap() {
        return this.additionalInformationMap;
    }

    public void clearPositionMap() {
        this.positionMap.clear();
    }

    public void addPositionToMap(final String key, final int value) {
        this.positionMap.put(key, value);
    }

    public void addInfoToMap(final String key, final Object value) {
        this.additionalInformationMap.put(key, value);
    }
}
