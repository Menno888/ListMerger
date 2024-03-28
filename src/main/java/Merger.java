import java.io.File;
import java.util.ArrayList;

public class Merger {

    public static final XMLParser xmlParser = new XMLParser();

    public static void merge(SongList songList, final String mergeFile) {
        final ArrayList<String> fileList = new ArrayList<>();
        final File folder = new File(System.getProperty("user.dir"));
        final File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            final String[] files = mergeFile.split(",");
            for (final File file : listOfFiles) {
                for (final String searchFile : files) {
                    if (file.isFile() && file.getName().endsWith(".xml") && !(ListExceptions.checkList(file.getName())) && file.getName().startsWith(searchFile)) {
                        fileList.add(file.getName());
                    }
                }
            }
        }

        for (final String file : fileList) {
            final File inFile = new File(file);
            final String fileToFeed = inFile.toString();
            songList = xmlParser.parseXML(fileToFeed, songList);
            System.out.println("There are currently " + songList.size() + " songs");
        }
    }
}
