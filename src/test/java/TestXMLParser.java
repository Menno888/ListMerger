import java.io.File;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class TestXMLParser {

    @Test
    void testReadDiacritics() {
        XMLParser parser = new XMLParser();
        SongList songList = new SongList();
        File inFile = FileUtil.getFileFromResource("testdiacritics.xml");
        String fileToFeed = inFile.toString();
        songList = parser.parseXML(fileToFeed, songList);

        Record firstRecord = songList.get(0);
        assertThat(firstRecord.getArtist()).isEqualTo("áéíäçèôñ");
        assertThat(firstRecord.getTitle()).isEqualTo(",.-=/+1234567890");
    }

    @Test
    void testReadOverriding() {
        XMLParser parser = new XMLParser();
        SongList songList = new SongList();
        File inFile = FileUtil.getFileFromResource("testoverriding.xml");
        String fileToFeed = inFile.toString();
        songList = parser.parseXML(fileToFeed, songList);

        Record firstRecord = songList.get(0);
        Record secondRecord = songList.get(1);
        assertThat(songList.size()).isEqualTo(2);
        assertThat(firstRecord.getPositionMap().size()).isEqualTo(2);
        assertThat(firstRecord.getPositionMap().get("VA2019")).isEqualTo(50);
        assertThat(secondRecord.getPositionMap().size()).isEqualTo(1);
        assertThat(secondRecord.getPositionMap().get("R2NLA2019")).isEqualTo(100);
    }
}
