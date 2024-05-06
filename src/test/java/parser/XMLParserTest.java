package parser;

import java.io.File;

import dto.Song;
import dto.SongList;
import org.junit.jupiter.api.Test;
import util.FileUtil;

import static org.assertj.core.api.Assertions.assertThat;

class XMLParserTest {

    @Test
    void testReadDiacritics() {
        final XMLParser parser = new XMLParser();
        SongList songList = new SongList();
        final File inFile = FileUtil.getFileFromResourceRootAndOtherwiseFromCustomPath("testdiacritics.xml");
        final String fileToFeed = inFile.toString();
        songList = parser.parseXML(fileToFeed, songList);

        final Song firstSong = songList.get(0);
        assertThat(firstSong.getArtist()).isEqualTo("áéíäçèôñ");
        assertThat(firstSong.getTitle()).isEqualTo(",.-=/+1234567890");
    }

    @Test
    void testReadOverriding() {
        final XMLParser parser = new XMLParser();
        SongList songList = new SongList();
        final File inFile = FileUtil.getFileFromResourceRootAndOtherwiseFromCustomPath("testoverriding.xml");
        final String fileToFeed = inFile.toString();
        songList = parser.parseXML(fileToFeed, songList);

        final Song firstSong = songList.get(0);
        final Song secondSong = songList.get(1);
        assertThat(songList).hasSize(2);
        assertThat(firstSong.getPositionMap()).hasSize(2);
        assertThat(firstSong.getPositionMap()).containsEntry("VA2019", 50);
        assertThat(secondSong.getPositionMap()).hasSize(1);
        assertThat(secondSong.getPositionMap()).containsEntry("R2NLA2019", 100);
    }
}
