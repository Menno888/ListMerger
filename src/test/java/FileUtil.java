import java.io.File;

public class FileUtil {

    //Try the root and if this fails, try the root + test/resources
    public static File getFileFromResource(final String fileName) {
        final File toReturn = new File(fileName);
        if (toReturn.length() != 0) {
            return toReturn;
        }
        else {
            return new File ("src/test/resources/" + fileName);
        }
    }
}
