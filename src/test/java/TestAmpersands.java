import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

class TestAmpersands {

    @Test
    void testReadAmpersands() {
        final XMLParser parser = new XMLParser();
        SongList songList = new SongList();
        final File inFile = FileUtil.getFileFromResource("testampersands.xml");
        final String fileToFeed = inFile.toString();
        songList = parser.parseXML(fileToFeed, songList);

        final Record firstRecord = songList.get(0);
        assertThat(firstRecord.getTitle()).isEqualTo("Music &amp; Lights");
        final Record secondRecord = songList.get(1);
        assertThat(secondRecord.getTitle()).isEqualTo("Music&amp;Lights");
    }
}
