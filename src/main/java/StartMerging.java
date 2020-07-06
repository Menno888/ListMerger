import java.io.*;
import java.util.*;

public class StartMerging {

    private static boolean takeInput = true;
    private static SongList songList = new SongList();
    private static final XMLParser xmlParser = new XMLParser();
    private static final ExcelParser excelParser = new ExcelParser();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (takeInput) {
            System.out.println("Enter your input ((c)lear, (f)ilter, (l)ist all in dir, (m)erge, (n)ormalize, (o)utput, (q)uit, (s)how current list, (t)ools), e(x)cel:");
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
                    merge(mergeFile);
                    break;
                case "n":
                    songList.normalize();
                    break;
                case "o":
                    System.out.println("Output to which filename?:");
                    String outFile = sc.nextLine();
                    System.out.println("Give options [positions, pretty-print, count]");
                    String optionsString = sc.nextLine();
                    String[] optionsArray = optionsString.split(",");
                    if ("".equals(optionsArray[0])) {
                        Writer.output(songList, outFile, new String[]{"y", "n", "n"});
                    } else {
                        Writer.output(songList, outFile, new String[]{optionsArray[0], optionsArray[1], optionsArray[2]});
                    }
                    break;
                case "q":
                    System.out.println("Goodbye");
                    takeInput = false;
                    break;
                case "s":
                    songList.outputToScreen();
                    break;
                case "t":
                    System.out.println("Soon (TM)");
                    break;
                case "x":
                    System.out.println("Enter an excel file to merge:");
                    String inExcel = sc.nextLine();
                    System.out.println("Enter a file name to output xml to:");
                    String outXml = sc.nextLine();
                    songList = excelParser.parseExcel(inExcel);
                    Writer.output(songList, outXml, new String[]{"y", "y", "n"});
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
        songList = xmlParser.parseXML(fileToFeed, songList);
        System.out.println("There are currently " + songList.size() + " records");
    }

    private static void initializeAndMerge() {
        ArrayList<String> fileList = new ArrayList<>(100);
        System.out.println("Enter a file name to output to:");
        String outFile = sc.nextLine();
        File folder = new File(System.getProperty("user.dir"));
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for(File file : listOfFiles) {
                if(file.isFile() && file.getName().endsWith(".xml") && !("themaother.xml".equals(file.getName()))) {
                    fileList.add(file.getName());
                }
            }
        }

        for (String file : fileList) {
            merge(file);
        }
        Writer.output(songList, outFile, new String[]{"y", "n", "n"});
        System.out.println("ALL LISTS CONVERTED!");
    }

    private static void filter(String lists) {
        String[] toKeep = lists.split(",");
        List<String> toKeepList = Arrays.asList(toKeep);
        ArrayList<String> allEditions = songList.tagCheckup();
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
                for (Map.Entry<String, Integer> entry2 : currentRecord.getPositionMap().entrySet()) {
                    if (newList.contains(entry2.getKey())) {
                        containsEntries = true;
                        break;
                    }
                }
                if(!containsEntries) {
                    currentRecord.cleanPositionMap();
                }
            }
            songList.normalize();
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
                for (Map.Entry<String, Integer> entry2 : currentRecord.getPositionMap().entrySet()) {
                    if (newList.contains(entry2.getKey())) {
                        containsEntries++;
                    }
                }
                if(containsEntries != newList.size()) {
                    currentRecord.cleanPositionMap();
                }
            }
            songList.normalize();
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
                for (Map.Entry<String, Integer> entry2 : currentRecord.getPositionMap().entrySet()) {
                    if (newList.contains(entry2.getKey())) {
                        containsEntries = true;
                        break;
                    }
                }
                if(containsEntries) {
                    currentRecord.cleanPositionMap();
                }
            }
            songList.normalize();
        }
    }
}