import java.util.LinkedHashMap;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class TestRecord {

    @Test
    void testRecord() {
        Record myRecord = new Record("Menno888", "The Life Of A Speedrunner");
        LinkedHashMap<String, Integer> recordMap = new LinkedHashMap<>();
        recordMap.put("GTATR100", 2742);
        myRecord.setPositionMap(recordMap);

        assertThat(myRecord.getArtiest()).isEqualTo("Menno888");
        assertThat(myRecord.getNummer()).isEqualTo("The Life Of A Speedrunner");
        assertThat(myRecord.getPositionMap().size()).isEqualTo(1);
        assertThat(myRecord.getPositionMap().get("GTATR100")).isEqualTo(2742);

        myRecord.setArtiest("Menno");
        myRecord.setNummer("The Troubled Times Of A Speedrunner");
        myRecord.addPositionToMap("LCU100", 1240);

        assertThat(myRecord.getArtiest()).isEqualTo("Menno");
        assertThat(myRecord.getNummer()).isEqualTo("The Troubled Times Of A Speedrunner");
        assertThat(myRecord.getPositionMap().size()).isEqualTo(2);
        assertThat(myRecord.getPositionMap().get("LCU100")).isEqualTo(1240);
    }
}