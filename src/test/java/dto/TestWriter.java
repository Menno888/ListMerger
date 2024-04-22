package dto;

import java.io.*;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class TestWriter {

    @Test
    void testWriteDiacritics() throws IOException {
        final SongList songList = new SongList();
        final Song song = new Song("áéíäçèôñ", ",.-=/+1234567890");
        song.addPositionToMap("List1", 1);
        song.addPositionToMap("List2", 2000);
        songList.add(song);

        songList.outputToFile( "testwritediacritics.xml", "y,y,y");

        final BufferedReader reader = new BufferedReader(new FileReader("testwritediacritics.xml"));
        final StringBuilder stringBuilder = new StringBuilder();
        String line;
        final String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        reader.close();

        final String content = stringBuilder.toString();
        assertThat(content).contains("áéí")
                .contains(",.-=/+")
                .contains("äçèôñ")
                .contains("1234567890")
                .contains("<appearances>2</appearances>")
                .contains("    ");

        final File toRemove = new File("testwritediacritics.xml");
        final boolean removedFile = toRemove.delete();
        System.out.println("Removed file with success value: " + removedFile);
    }
}
