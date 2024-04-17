package tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ListExceptions {

    private static final ArrayList<String> list = new ArrayList<>();

    private ListExceptions() {
        //No-args
    }

    public static boolean checkList(final String listName) {
        try {
            final Scanner scanner = new Scanner(new File("list.exceptions"));
            while (scanner.hasNext()) {
                list.add(scanner.next());
            }
            scanner.close();
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        return list.contains(listName);
    }
}
