package service;

import dto.SongList;
import filter.Filter;
import merger.Merger;
import parser.ExcelParser;
import tools.ToplijstenMergerTools;

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
            switch (control) {
                case "c" -> clearSongList();
                case "f" -> filterSongList();
                case "m" -> mergeSongList();
                case "n" -> songList.normalize();
                case "o" -> outputSongList();
                case "q" -> quit();
                case "s" -> songList.outputToScreen();
                case "t" -> ToplijstenMergerTools.getTools(songList);
                case "x" -> parseExcelFile();
                default -> System.out.println("Invalid input, try again");
            }
        }
    }

    private static void clearSongList() {
        songList.clear();
        System.out.println("Current list cleared");
    }

    private static void filterSongList() {
        System.out.println("Filter lists (fl) or years (fy)?");
        String filterOption = sc.nextLine();
        System.out.println("Type list abbreviation/year or set of list abbreviations/years separated by commas to filter on");
        String toKeep = sc.nextLine();
        Filter.filter(songList, toKeep, filterOption);
    }

    private static void mergeSongList() {
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

    private static void outputSongList() {
        System.out.println("Output to which filename?:");
        String outFileOutput = sc.nextLine();
        System.out.println("Give options [positions, pretty-print, count]");
        String optionsString = sc.nextLine();
        songList.outputToFile(outFileOutput, optionsString);
    }

    private static void quit() {
        System.out.println("Goodbye");
        takeInput = false;
    }

    private static void parseExcelFile() {
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
}
