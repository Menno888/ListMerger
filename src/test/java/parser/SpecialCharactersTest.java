package parser;

import dto.Song;
import dto.SongList;
import org.junit.jupiter.api.Test;
import util.FileUtil;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

class SpecialCharactersTest {

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

    @Test
    void testRecord() {
        final ExcelParser parser = new ExcelParser();
        final File inFile = FileUtil.getFileFromResourceRootAndOtherwiseFromCustomPath("testexcel.xlsx");
        final String fileToFeed = inFile.toString();
        final SongList songList = parser.parseExcel(fileToFeed);

        final Song firstSong = songList.get(0);
        final Song secondSong = songList.get(1);
        final Song thirdSong = songList.get(2);
        assertThat(songList).hasSize(3);
        assertThat(firstSong.getPositionMap()).hasSize(1);
        assertThat(secondSong.getArtist()).isEqualTo("áéíäçèôñ");
        assertThat(secondSong.getTitle()).isEqualTo(",.-=/+1234567890");
        assertThat(thirdSong.getPositionMap()).hasSize(2);
        assertThat(firstSong.getPositionMap()).containsKey("R2NLA2019");
        assertThat(secondSong.getPositionMap()).containsKey("VA2019");
    }
}
