package merger;

import dto.SongList;
import org.apache.commons.io.FileUtils;
import parser.XMLParser;
import tools.ListExceptions;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Objects.nonNull;

public class Merger {

    public static final XMLParser xmlParser = new XMLParser();

    private Merger() {
        //No-args
    }

    public static void merge(SongList songList, final String mergeFile) {
        List<File> filesToProcess = new ArrayList<>();
        final File rootFolder = new File(System.getProperty("user.dir"));
        Collection<File> filesInWorkingDir = FileUtils.listFiles(rootFolder, new String[]{"xml"}, true);
        final String[] filesToMerge = mergeFile.split(",");

        if (nonNull(filesInWorkingDir)) {
            filesToProcess = filesInWorkingDir
                    .stream()
                    .filter(e -> shouldListBeAdded(e, filesToMerge))
                    .toList();
        } else {
            System.out.println("No files in working directory, skipping");
        }

        for (final File fileToProcess : filesToProcess) {
            final String fileToFeed = fileToProcess.toString();
            songList = xmlParser.parseXML(fileToFeed, songList);
            System.out.println("There are currently " + songList.size() + " songs");
        }
    }

    private static boolean shouldListBeAdded(final File fileInDir, final String[] filesToMerge) {
        boolean existsAndStartsWith = false;
        for (final String fileToMerge : filesToMerge) {
            if (fileInDir.isFile() &&
                    !(ListExceptions.isListToBeExcluded(fileInDir.getName())) &&
                    fileInDir.getName().startsWith(fileToMerge)) {
                existsAndStartsWith = true;
            }
        }
        return existsAndStartsWith;
    }
}
