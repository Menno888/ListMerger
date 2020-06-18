import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public final class SongExceptions {

    private static final Map<String, String> exceptions = new HashMap<>();

    public static Map loadExceptions() {
        final InputStream inputStream = SongExceptions.class.getClassLoader().getResourceAsStream("song.exceptions");
        try {
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
        return exceptions;
    }

    public static Record songExceptionConverter(Record record) {
        return record;
        //TODO Add implementation
    }
}