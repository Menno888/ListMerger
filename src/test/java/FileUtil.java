import java.io.File;

public class FileUtil {

    private static final String TEST_RESOURCE_PATH = "src/test/resources/";

    public static File getFileFromResourceRootAndOtherwiseFromCustomPath(final String fileName) {
        final File toReturn = new File(fileName);
        if (toReturn.length() != 0) {
            return toReturn;
        }
        else {
            return new File(TEST_RESOURCE_PATH + fileName);
        }
    }
}
