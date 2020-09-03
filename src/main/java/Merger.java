import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Merger {

    public static final XMLParser xmlParser = new XMLParser();

    public static void merge(SongList songList, String mergeFile) {
        ArrayList<String> fileList = new ArrayList<>();
        if("".equals(mergeFile)) {
            File folder = new File(System.getProperty("user.dir"));
            File[] listOfFiles = folder.listFiles();

            if (listOfFiles != null) {
                for(File file : listOfFiles) {
                    if(file.isFile() && file.getName().endsWith(".xml") && !(ListExceptions.checkList(file.getName()))) {
                        fileList.add(file.getName());
                    }
                }
            }
        }
        else {
            String[] files = mergeFile.split(",");
            fileList.addAll(Arrays.asList(files));
        }

        for (String file : fileList) {
            File inFile = new File(file);
            String fileToFeed = inFile.toString();
            songList = xmlParser.parseXML(fileToFeed, songList);
            System.out.println("There are currently " + songList.size() + " records");
        }
    }
}
