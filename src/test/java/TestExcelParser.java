import java.io.File;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class TestExcelParser {

    @Test
    void testRecord() {
        ExcelParser parser = new ExcelParser();
        File inFile = FileUtil.getFileFromResource("testexcel.xlsx");
        String fileToFeed = inFile.toString();
        SongList songList = parser.parseExcel(fileToFeed);

        Record firstRecord = songList.get(0);
        Record secondRecord = songList.get(1);
        Record thirdRecord = songList.get(2);
        assertThat(songList.size()).isEqualTo(3);
        assertThat(firstRecord.getPositionMap().size()).isEqualTo(1);
        assertThat(secondRecord.getArtist()).isEqualTo("áéíäçèôñ");
        assertThat(secondRecord.getTitle()).isEqualTo(",.-=/+1234567890");
        assertThat(thirdRecord.getPositionMap().size()).isEqualTo(2);
        assertThat(firstRecord.getPositionMap().containsKey("R2NLA2019")).isTrue();
        assertThat(secondRecord.getPositionMap().containsKey("VA2019")).isTrue();
    }
}