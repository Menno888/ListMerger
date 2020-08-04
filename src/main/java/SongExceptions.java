import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class SongExceptions {

    private static final Map<String, String> exceptions = new HashMap<>();

    public SongExceptions() {}

    public static void loadExceptions() {
        final InputStream inputStream = SongExceptions.class.getClassLoader().getResourceAsStream("song.exceptions");
        try {
            assert inputStream != null : "Input stream is null";
            final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            final BufferedReader reader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    final String[] splitLines = line.split("\\|");
                    exceptions.put(splitLines[0] + "|" + splitLines[1], splitLines[2] + "|" + splitLines[3]);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void songExceptionConverter(Record record) {
        final String songToChange = record.getArtist() + "|" + record.getTitle();
        if (exceptions.containsKey(songToChange)) {
            String oldSong = record.getArtist() + " - " + record.getTitle();
            final String[] newSongValues = exceptions.get(songToChange).split("\\|");
            record.setArtist(newSongValues[0]);
            record.setTitle(newSongValues[1]);
            //System.out.println("Changed " + oldSong + " to " + newSongValues[0] + " - " + newSongValues[1]);
        }
    }
}
