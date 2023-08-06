import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class TestRecord {

    @Test
    void testRecord() {
        final Record myRecord = new Record("Menno888", "The Life Of A Speedrunner");
        myRecord.addPositionToMap("GTATR100", 2742);

        assertThat(myRecord.getArtist()).isEqualTo("Menno888");
        assertThat(myRecord.getTitle()).isEqualTo("The Life Of A Speedrunner");
        assertThat(myRecord.getPositionMap()).hasSize(1);
        assertThat(myRecord.getPositionMap()).containsEntry("GTATR100", 2742);

        myRecord.setArtist("Menno");
        myRecord.setTitle("The Troubled Times Of A Speedrunner");
        myRecord.addPositionToMap("LCU100", 1240);

        assertThat(myRecord.getArtist()).isEqualTo("Menno");
        assertThat(myRecord.getTitle()).isEqualTo("The Troubled Times Of A Speedrunner");
        assertThat(myRecord.getPositionMap()).hasSize(2);
        assertThat(myRecord.getPositionMap()).containsEntry("LCU100", 1240);
    }
}