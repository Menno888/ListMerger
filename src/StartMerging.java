import java.io.*;
import java.util.*;

public class StartMerging {

    private static boolean takeInput = true;
    private static ArrayList<Record> songList = new ArrayList<>(50000);
    private static BreakXMLFile breaker = new BreakXMLFile();
    private static Scanner sc = new Scanner(System.in);
    private static Writer writer = new Writer();

    public static void main(String[] args) {
        while (takeInput) {
            System.out.println("Enter your input ((c)lear, (f)ilter, (l)ist all in dir, (m)erge, (n)ormalize, (o)utput, (q)uit, (s)how current list, (t)ools):");
            String control = sc.nextLine();
            switch (control) {
                case "c":
                    songList.clear();
                    System.out.println("Current list cleared");
                    break;
                case "f":
                    System.out.println("Type lists or a set of list");
                    String toKeep = sc.nextLine();
                    filter(toKeep);
                    break;
                case "l":
                    initializeAndMerge();
                    takeInput = false;
                    break;
                case "m":
                    System.out.println("Enter a file to initialize/merge:");
                    String mergeFile = sc.nextLine();
                    if (!mergeFile.endsWith(".xml")) {
                        mergeFile = mergeFile + ".xml";
                    }
                    merge(mergeFile);
                    break;
                case "n":
                    normalize();
                    break;
                case "o":
                    System.out.println("Output to which filename?:");
                    String outFile = sc.nextLine();
                    if ("".equals(outFile)) {
                        outFile = System.currentTimeMillis() + ".xml";
                    }
                    if (!outFile.endsWith(".xml")) {
                        outFile = outFile + ".xml";
                    }
                    System.out.println("Give options [positions, pretty-print, count]");
                    String optionsString = sc.nextLine();
                    String[] optionsArray = optionsString.split(",");
                    if ("".equals(optionsArray[0])) {
                        Writer.output(songList, outFile, "y", "n", "n");
                    } else {
                        Writer.output(songList, outFile, optionsArray[0], optionsArray[1], optionsArray[2]);
                    }
                    break;
                case "t":
                    for (Record song : songList) {
                        if (!"Beatles".equals(song.getArtiest())) {
                            song.cleanPositionMap();
                        }
                    }
                    normalize();
                    break;
                case "q":
                    System.out.println("Goodbye");
                    takeInput = false;
                    break;
                case "s":
                    outputToScreen();
                    break;
                default:
                    System.out.println("Invalid input, try again");
                    break;
            }
        }
    }

    private static void merge(String mergeFile) {
        File inFile = new File(mergeFile);
        String fileToFeed = inFile.toString();
        songList = breaker.startBreak(fileToFeed, songList);
        System.out.println("There are currently " + songList.size() + " records");
    }

    private static void outputToScreen() {
        for(Record r : songList) {
            System.out.println(r.showSong());
        }
        System.out.println("There's " + songList.size() + " records left");
    }

    private static void initializeAndMerge() {
        ArrayList<String> fileList = new ArrayList<>(100);
        System.out.println("Enter a file name to output to:");
        String outFile = sc.nextLine();
        if ("".equals(outFile)) {
            outFile = System.currentTimeMillis() + ".xml";
        }
        if (!outFile.endsWith(".xml")) {
            outFile = outFile + ".xml";
        }
        File folder = new File(System.getProperty("user.dir"));
        File[] listOfFiles = folder.listFiles();

        for(File file : listOfFiles) {
            if(file.isFile() && file.getName().endsWith(".xml") && !("themaother.xml".equals(file.getName()))) {
                fileList.add(file.getName());
            }
        }

        for (String file : fileList) {
            merge(file);
        }
        Writer.output(songList, outFile, "y", "n", "n");
        System.out.println("ALL LISTS CONVERTED!");
    }

    private static void filter(String lists) {
        String[] toKeep = lists.split(",");
        List<String> toKeepList = Arrays.asList(toKeep);
        ArrayList<String> allEditions = tagCheckup();
        List<String> newList = new ArrayList<>();
        for (String edition : allEditions) {
            if (toKeepList.contains(edition.substring(0, edition.length() - 4)) || toKeepList.contains(edition)) {
                newList.add(edition);
            }
        }
        String shouldAppearInAll = "n";
        String retainUnused = "n";
        System.out.println("Should appear [y] or not [n] appear in specified lists?");
        String shouldAppear = sc.nextLine();
        if(newList.size() > 1 && "y".equals(shouldAppear)) {
            System.out.println("Should appear in all lists or not? [y/n]");
            shouldAppearInAll = sc.nextLine();
        }
        if("y".equals(shouldAppear)) {
            System.out.println("Should retain lists that aren't in the selection?");
            retainUnused = sc.nextLine();
        }
        if("y".equals(shouldAppear) && "n".equals(shouldAppearInAll)) {
            for(Record currentRecord : songList) {
                boolean containsEntries = false;
                for (Map.Entry<String, Object> entry2 : currentRecord.getPositionMap().entrySet()) {
                    if (newList.contains(entry2.getKey())) {
                        containsEntries = true;
                    }
                }
                if(!containsEntries) {
                    currentRecord.cleanPositionMap();
                }
            }
            normalize();
            allEditions.removeAll(newList);
            if("n".equals(retainUnused)) {
                for(Record currentRecord : songList) {
                    for(String edition : allEditions) {
                        currentRecord.getPositionMap().remove(edition);
                    }
                }
            }
        }
        else if("y".equals(shouldAppear) && "y".equals(shouldAppearInAll)) {
            for(Record currentRecord : songList) {
                int containsEntries = 0;
                for (Map.Entry<String, Object> entry2 : currentRecord.getPositionMap().entrySet()) {
                    if (newList.contains(entry2.getKey())) {
                        containsEntries++;
                    }
                }
                if(containsEntries != newList.size()) {
                    currentRecord.cleanPositionMap();
                }
            }
            normalize();
            allEditions.removeAll(newList);
            if("n".equals(retainUnused)) {
                for(Record currentRecord : songList) {
                    for(String edition : allEditions) {
                        currentRecord.getPositionMap().remove(edition);
                    }
                }
            }
        }
        else {
            for(Record currentRecord : songList) {
                boolean containsEntries = false;
                for (Map.Entry<String, Object> entry2 : currentRecord.getPositionMap().entrySet()) {
                    if (newList.contains(entry2.getKey())) {
                        containsEntries = true;
                    }
                }
                if(containsEntries) {
                    currentRecord.cleanPositionMap();
                }
            }
            normalize();
        }
    }

    private static void normalize() {
        Iterator<Record> it = songList.iterator();
        while(it.hasNext()) {
            Record item = it.next();
            if(item.getPositionMap().size() == 0) {
                it.remove();
                System.out.println("Removed " + item.showSong());
            }
        }
        System.out.println("There's " + songList.size() + " records left");
    }

    private static ArrayList<String> tagCheckup() {
        ArrayList<String> tagList = new ArrayList<>();
        for(Record currentRecord : songList) {
            for(Map.Entry<String, Object> entry : currentRecord.getPositionMap().entrySet()) {
                if(!tagList.contains(entry.getKey())) {
                    tagList.add(entry.getKey());
                }
            }
        }
        return tagList;
    }
}