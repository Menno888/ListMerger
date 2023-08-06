import java.io.File;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class TestXMLParser {

    @Test
    void testReadDiacritics() {
        final XMLParser parser = new XMLParser();
        SongList songList = new SongList();
        final File inFile = FileUtil.getFileFromResource("testdiacritics.xml");
        final String fileToFeed = inFile.toString();
        songList = parser.parseXML(fileToFeed, songList);

        final Record firstRecord = songList.get(0);
        assertThat(firstRecord.getArtist()).isEqualTo("áéíäçèôñ");
        assertThat(firstRecord.getTitle()).isEqualTo(",.-=/+1234567890");
    }

    @Test
    void testReadOverriding() {
        final XMLParser parser = new XMLParser();
        SongList songList = new SongList();
        final File inFile = FileUtil.getFileFromResource("testoverriding.xml");
        final String fileToFeed = inFile.toString();
        songList = parser.parseXML(fileToFeed, songList);

        final Record firstRecord = songList.get(0);
        final Record secondRecord = songList.get(1);
        assertThat(songList).hasSize(2);
        assertThat(firstRecord.getPositionMap()).hasSize(2);
        assertThat(firstRecord.getPositionMap()).containsEntry("VA2019", 50);
        assertThat(secondRecord.getPositionMap()).hasSize(1);
        assertThat(secondRecord.getPositionMap()).containsEntry("R2NLA2019", 100);
    }
}
