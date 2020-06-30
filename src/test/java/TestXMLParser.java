import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class TestXMLParser {

    @Test
    void testReadDiacritics() {
        XMLParser parser = new XMLParser();
        ArrayList<Record> songList = new ArrayList<>(5);
        File inFile = new File("testdiacritics.xml");
        String fileToFeed = inFile.toString();
        songList = parser.parseXML(fileToFeed, songList);

        Record firstRecord = songList.get(0);
        assertThat(firstRecord.getArtiest()).contains("áéíäçèôñ");
        assertThat(firstRecord.getNummer()).contains(",.-=/+1234567890");
    }

    @Test
    void testReadOverriding() {
        XMLParser parser = new XMLParser();
        ArrayList<Record> songList = new ArrayList<>(5);
        File inFile = new File("testoverriding.xml");
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

    @Test
    void testReadExceptions() {
        XMLParser parser = new XMLParser();
        ArrayList<Record> songList = new ArrayList<>(5);
        File inFile = new File("testsongexceptions.xml");
        String fileToFeed = inFile.toString();
        songList = parser.parseXML(fileToFeed, songList);

        Record firstRecord = songList.get(0);
        Record secondRecord = songList.get(1);
        assertThat(songList.size()).isEqualTo(2);
        assertThat(firstRecord.getArtiest()).isEqualTo("Y&amp;T");
        assertThat(secondRecord.getArtiest()).isEqualTo("Y &amp; T");
    }
}
