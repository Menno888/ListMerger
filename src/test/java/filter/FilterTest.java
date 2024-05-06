package filter;

import dto.Song;
import dto.SongList;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.MethodName.class)
class FilterTest {

    static final String INPUT_FOR_TESTS = "ynnynnynynynnynn";

    static final InputStream sysInBackup = System.in;

    private SongList songList;

    @BeforeAll
    static void setUpUserInputString() {
        ByteArrayInputStream userInputForAllTests = new ByteArrayInputStream(createOrderOfInputsFromString().getBytes());
        System.setIn(userInputForAllTests);
    }

    @BeforeEach
    void setUp() {
        songList = createSongListForTests();
    }

    //Input: y,n,n
    @Test
    void testFilterAAllFromOneSource() {
        Filter.filter(songList, "LIST", "fl");
        assertThat(songList.tagCheckup()).containsExactlyInAnyOrder("LISTA1999", "LISTA1999B", "LISTAB1999", "LISTA2000", "LIST01999", "LIST82000", "LIST80000");
    }

    //Input: y,n,n
    @Test
    void testFilterBOneSourceAndCategory() {
        Filter.filter(songList, "LISTA", "fl");
        assertThat(songList.tagCheckup()).containsExactlyInAnyOrder("LISTA1999", "LISTA2000");
    }

    //Input: y,n
    @Test
    void testFilterCOneSourceAndCategoryWithTwoLetters() {
        Filter.filter(songList, "LISTAB", "fl");
        assertThat(songList.tagCheckup()).containsExactly("LISTAB1999");
    }

    //Input: y,n
    @Test
    void testFilterDOneSourceAndCategoryAndPartOfYear() {
        Filter.filter(songList, "LISTA1", "fl");
        assertThat(songList.tagCheckup()).containsExactly("LISTA1999");
    }

    //Input: y,n,n
    @Test
    void testFilterEOneSourceAndNumericCategory() {
        Filter.filter(songList, "LIST8", "fl");
        assertThat(songList.tagCheckup()).containsExactly("LIST82000", "LIST80000");
    }

    //Input: y,n,n
    @Test
    void testFilterFOneYear() {
        Filter.filter(songList, "1999", "fy");
        assertThat(songList.tagCheckup()).containsExactlyInAnyOrder("LISTA1999", "LISTA1999B", "LISTAB1999", "LIST01999", "NOPEA1999");
    }

    @AfterAll
    static void tearDown() {
        System.setIn(sysInBackup);
    }

    private static String createOrderOfInputsFromString() {
        final List<String> arrayOfInputs = Arrays.stream(INPUT_FOR_TESTS.split("")).collect(Collectors.toList());
        return String.join(System.lineSeparator(), arrayOfInputs);
    }

    private static SongList createSongListForTests() {
        final SongList songListForTests = new SongList();
        final Song song1 = new Song("My Artist", "My Title1");
        song1.addPositionToMap("LISTA1999", 1);
        songListForTests.add(song1);
        final Song song2 = new Song("My Artist", "My Title2");
        song2.addPositionToMap("LISTA1999B", 1);
        songListForTests.add(song2);
        final Song song3 = new Song("My Artist", "My Title3");
        song3.addPositionToMap("LISTAB1999", 1);
        songListForTests.add(song3);
        final Song song4 = new Song("My Artist", "My Title4");
        song4.addPositionToMap("LISTA2000", 1);
        songListForTests.add(song4);
        final Song song5 = new Song("My Artist", "My Title5");
        song5.addPositionToMap("LIST01999", 1);
        songListForTests.add(song5);
        final Song song6 = new Song("My Artist", "My Title6");
        song6.addPositionToMap("LIST82000", 1);
        songListForTests.add(song6);
        final Song song7 = new Song("My Artist", "My Title7");
        song7.addPositionToMap("NOPEA1999", 1);
        songListForTests.add(song7);
        final Song song8 = new Song("My Artist", "My Title8");
        song8.addPositionToMap("LIST80000", 1);
        songListForTests.add(song8);
        return songListForTests;
    }
}
