import java.util.*;

public class StartMerging {

    private static boolean takeInput = true;
    private static SongList songList = new SongList();
    private static final ExcelParser excelParser = new ExcelParser();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (takeInput) {
            System.out.println("Enter your input ((c)lear, (f)ilter, (m)erge, (n)ormalize, (o)utput, (q)uit, (s)how current list, (t)ools), e(x)cel:");
            String control = sc.nextLine();
            switch (control) {
                case "c":
                    songList.clear();
                    System.out.println("Current list cleared");
                    break;
                case "f":
                    System.out.println("Type lists or a set of list");
                    String toKeep = sc.nextLine();
                    Filter.filter(songList, toKeep);
                    break;
                case "m":
                    System.out.println("Enter a file or multiple files separated by commas to merge, leave blank for all lists in working dir (without lists in list.exceptions):");
                    String mergeFile = sc.nextLine();
                    if("".equals(mergeFile)) {
                        System.out.println("Enter a file name to output to:");
                        String outFileMerge = sc.nextLine();
                        Merger.merge(songList, mergeFile);
                        songList.outputToFile(outFileMerge, "y,n,n");
                    }
                    else {
                        Merger.merge(songList, mergeFile);
                    }
                    break;
                case "n":
                    songList.normalize();
                    break;
                case "o":
                    System.out.println("Output to which filename?:");
                    String outFileOutput = sc.nextLine();
                    System.out.println("Give options [positions, pretty-print, count]");
                    String optionsString = sc.nextLine();
                    songList.outputToFile(outFileOutput, optionsString);
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
                    songList.outputToFile(outXml, "y,y,n");
                    songList.clear();
                    break;
                default:
                    System.out.println("Invalid input, try again");
                    break;
            }
        }
    }
}
