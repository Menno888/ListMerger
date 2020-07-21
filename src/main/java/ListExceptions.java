import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ListExceptions {

    private static final ArrayList<String> list = new ArrayList<>();

    public ListExceptions() {}

    public static boolean checkList(String listName) {
        try {
            Scanner scanner = new Scanner(new File("list.exceptions"));
            while (scanner.hasNext()) {
                list.add(scanner.next());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return list.contains(listName);
    }
}
