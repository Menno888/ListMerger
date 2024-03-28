package dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class TestSong {

    @Test
    void testRecord() {
        final Song mySong = new Song("Menno888", "The Life Of A Speedrunner");
        mySong.addPositionToMap("GTATR100", 2742);

        assertThat(mySong.getArtist()).isEqualTo("Menno888");
        assertThat(mySong.getTitle()).isEqualTo("The Life Of A Speedrunner");
        assertThat(mySong.getPositionMap()).hasSize(1);
        assertThat(mySong.getPositionMap()).containsEntry("GTATR100", 2742);

        mySong.setArtist("Menno");
        mySong.setTitle("The Troubled Times Of A Speedrunner");
        mySong.addPositionToMap("LCU100", 1240);

        assertThat(mySong.getArtist()).isEqualTo("Menno");
        assertThat(mySong.getTitle()).isEqualTo("The Troubled Times Of A Speedrunner");
        assertThat(mySong.getPositionMap()).hasSize(2);
        assertThat(mySong.getPositionMap()).containsEntry("LCU100", 1240);
    }
}