import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

class TestAmpersands {

    @Test
    void testReadAmpersands() {
        XMLParser parser = new XMLParser();
        SongList songList = new SongList();
        File inFile = FileUtil.getFileFromResource("testampersands.xml");
        String fileToFeed = inFile.toString();
        songList = parser.parseXML(fileToFeed, songList);

        Record firstRecord = songList.get(0);
        assertThat(firstRecord.getTitle()).isEqualTo("Music &amp; Lights");
        Record secondRecord = songList.get(1);
        assertThat(secondRecord.getTitle()).isEqualTo("Music&amp;Lights");
    }
}
