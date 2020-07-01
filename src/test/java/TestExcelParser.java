import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class TestExcelParser {

    @Test
    void testRecord() {
        ExcelParser parser = new ExcelParser();
        File inFile = new File("testexcel.xlsx");
        String fileToFeed = inFile.toString();
        ArrayList<Record> songList = parser.parseExcel(fileToFeed);

        Record firstRecord = songList.get(0);
        Record secondRecord = songList.get(1);
        Record thirdRecord = songList.get(2);
        assertThat(songList.size()).isEqualTo(3);
        assertThat(firstRecord.getPositionMap().size()).isEqualTo(1);
        assertThat(secondRecord.getArtiest()).isEqualTo("áéíäçèôñ");
        assertThat(secondRecord.getNummer()).isEqualTo(",.-=/+1234567890");
        assertThat(thirdRecord.getPositionMap().size()).isEqualTo(2);
    }
}