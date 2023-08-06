import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Merger {

    public static final XMLParser xmlParser = new XMLParser();

    public static void merge(SongList songList, final String mergeFile) {
        final ArrayList<String> fileList = new ArrayList<>();
        if ("".equals(mergeFile)) {
            final File folder = new File(System.getProperty("user.dir"));
            final File[] listOfFiles = folder.listFiles();

            if (listOfFiles != null) {
                for (final File file : listOfFiles) {
                    if (file.isFile() && file.getName().endsWith(".xml") && !(ListExceptions.checkList(file.getName()))) {
                        fileList.add(file.getName());
                    }
                }
            }
        }
        else {
            final String[] files = mergeFile.split(",");
            fileList.addAll(Arrays.asList(files));
        }

        for (final String file : fileList) {
            final File inFile = new File(file);
            final String fileToFeed = inFile.toString();
            songList = xmlParser.parseXML(fileToFeed, songList);
            System.out.println("There are currently " + songList.size() + " records");
        }
    }
}
