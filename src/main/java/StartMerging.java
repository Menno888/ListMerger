import java.util.*;

public class StartMerging {

    private static boolean takeInput = true;
    private static SongList songList = new SongList();
    private static final ExcelParser excelParser = new ExcelParser();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (takeInput) {
            System.out.println("Enter your input ((c)lear, (f)ilter, (m)erge, (n)ormalize, (o)utput, (q)uit, (s)how current list, (t)ools, e(x)cel:");
            String control = sc.nextLine();
            if ("c".equals(control)) {
                songList.clear();
                System.out.println("Current list cleared");
            }
            else if ("f".equals(control)) {
                System.out.println("Type lists or a set of list");
                String toKeep = sc.nextLine();
                Filter.filter(songList, toKeep);
            }
            else if ("m".equals(control)) {
                System.out.println("Enter a file or multiple files separated by commas to merge, leave blank for all lists in working dir (without lists in list.exceptions):");
                String mergeFile = sc.nextLine();
                if ("".equals(mergeFile)) {
                    System.out.println("Enter a file name to output to:");
                    String outFileMerge = sc.nextLine();
                    Merger.merge(songList, mergeFile);
                    songList.outputToFile(outFileMerge, "y,n,n");
                }
                else {
                    Merger.merge(songList, mergeFile);
                }
            }
            else if ("n".equals(control)) {
                songList.normalize();
            }
            else if ("o".equals(control)) {
                System.out.println("Output to which filename?:");
                String outFileOutput = sc.nextLine();
                System.out.println("Give options [positions, pretty-print, count]");
                String optionsString = sc.nextLine();
                songList.outputToFile(outFileOutput, optionsString);
            }
            else if ("q".equals(control)) {
                System.out.println("Goodbye");
                takeInput = false;
            }
            else if ("s".equals(control)) {
                songList.outputToScreen();
            }
            else if ("t".equals(control)) {
                ToplijstenMergerTools.getTools(songList);
            }
            else if ("x".equals(control)) {
                System.out.println("Enter an excel file to merge:");
                String inExcel = sc.nextLine();
                System.out.println("Enter a file name to output xml to (leave blank for original file name):");
                String outXml = sc.nextLine();
                songList = excelParser.parseExcel(inExcel);
                if ("".equals(outXml)) {
                    songList.outputToFile(inExcel, "y,y,n");
                } else {
                    songList.outputToFile(outXml, "y,y,n");
                }
                songList.clear();
            }
            else {
                System.out.println("Invalid input, try again");
            }
        }
    }
}
