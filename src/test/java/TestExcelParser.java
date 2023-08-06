import java.io.File;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class TestExcelParser {

    @Test
    void testRecord() {
        final ExcelParser parser = new ExcelParser();
        final File inFile = FileUtil.getFileFromResource("testexcel.xlsx");
        final String fileToFeed = inFile.toString();
        final SongList songList = parser.parseExcel(fileToFeed);

        final Record firstRecord = songList.get(0);
        final Record secondRecord = songList.get(1);
        final Record thirdRecord = songList.get(2);
        assertThat(songList).hasSize(3);
        assertThat(firstRecord.getPositionMap()).hasSize(1);
        assertThat(secondRecord.getArtist()).isEqualTo("áéíäçèôñ");
        assertThat(secondRecord.getTitle()).isEqualTo(",.-=/+1234567890");
        assertThat(thirdRecord.getPositionMap()).hasSize(2);
        assertThat(firstRecord.getPositionMap()).containsKey("R2NLA2019");
        assertThat(secondRecord.getPositionMap()).containsKey("VA2019");
    }
}