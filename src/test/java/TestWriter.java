import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class TestWriter {

    @Test
    void testDiacritics() throws IOException {
        ArrayList<Record> songList = new ArrayList<>(50000);
        Record record = new Record("áéíäçèôñ", ",.-=/+1234567890");
        LinkedHashMap<String, Integer> recordMap = new LinkedHashMap<>();
        recordMap.put("List1", 1);
        recordMap.put("List2", 2000);
        record.setPositionMap(recordMap);
        songList.add(record);

        Writer.output(songList, "testDiacritics.xml", "y", "y", "y");

        BufferedReader reader = new BufferedReader(new FileReader("testDiacritics.xml"));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
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
        assertThat(content).contains("<appearances>2</appearances>");
        assertThat(content).contains("    ");

        File toRemove = new File("testDiacritics.xml");
        toRemove.delete();
    }
}
