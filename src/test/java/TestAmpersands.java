import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

class TestAmpersands {

    @Test
    void testReadAmpersands() {
        final XMLParser parser = new XMLParser();
        SongList songList = new SongList();
        final File inFile = FileUtil.getFileFromResourceRootAndOtherwiseFromCustomPath("testampersands.xml");
        final String fileToFeed = inFile.toString();
        songList = parser.parseXML(fileToFeed, songList);

        final Song firstSong = songList.get(0);
        assertThat(firstSong.getTitle()).isEqualTo("Music &amp; Lights");
        final Song secondSong = songList.get(1);
        assertThat(secondSong.getTitle()).isEqualTo("Music&amp;Lights");
    }
}
