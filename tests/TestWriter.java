import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestWriter {

    @Test
    void testDiacritics() throws IOException {
        ArrayList<Record> songList = new ArrayList<Record>(50000);
        Record r1 = new Record("áéí", ",.-=/+");
        Record r2 = new Record("äçèôñ", "1234567890");
        songList.add(r1);
        songList.add(r2);

        Writer.output(songList, "testDiacritics.xml", "y", "y", "y");

        BufferedReader reader = new BufferedReader(new FileReader("testDiacritics.xml"));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        reader.close();

        String content = stringBuilder.toString();
        assertThat(content).contains("áéí");
        assertThat(content).contains(",.-=/+");
        assertThat(content).contains("äçèôñ");
        assertThat(content).contains("1234567890");

        File toRemove = new File("testDiacritics.xml");
        toRemove.delete();
    }
}
