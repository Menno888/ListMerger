import java.util.*;
import java.util.regex.Pattern;

import static java.util.Objects.nonNull;

public class ToplijstenMergerTools {

    private static final Scanner sc = new Scanner(System.in);

    public static void getTools(SongList list) {
        boolean takeInput = true;
        while(takeInput) {
            System.out.println("Tools: (h)ighest (p)ositions, (w)ithout (p)ositions, count (re)entries, (p)oint (l)ist, (q)uit");
            String control = sc.nextLine();
            if ("hp".equals(control)) {
                SongList newList = new SongList();
                for (Record song : list) {
                    Record r = new Record();
                    r.setArtist(song.getArtist());
                    r.setTitle(song.getTitle());
                    if (song.getPositionMap().size() > 0) {
                        int max = song.getPositionMap().values().stream().min(Comparator.naturalOrder()).get();
                        r.addPositionToMap("MAX", max);
                    }
                    newList.add(r);
                }
                newList.outputToFile();
                System.out.println("Wrote current SongList with highest positions");
            }
            else if ("wp".equals(control)) {
                for (Record r : list) {
                    r.cleanPositionMap();
                }
                System.out.println("Done cleaning positions");
            }
            else if ("re".equals(control)) {
                System.out.println("Give the lists separated by commas:");
                String lists = sc.nextLine();
                SongList newList = new SongList();
                String[] reentryList = lists.split(",");
                for (Record record : list) {
                    Record r = new Record();
                    r.setArtist(record.getArtist());
                    r.setTitle(record.getTitle());
                    int total = 0;
                    for (int j = 0; j < reentryList.length - 1; j++) {
                        Integer p1 = record.getPositionMap().get(reentryList[j]);
                        Integer p2 = record.getPositionMap().get(reentryList[j + 1]);
                        if (!(p1 == null) && (p2 == null || p2 == 0)) {
                            total++;
                        }
                    }
                    if (record.getPositionMap().get(reentryList[reentryList.length - 1]) != null) {
                        total++;
                    }
                    r.addPositionToMap("NUM", total);
                    newList.add(r);
                }
                newList.outputToFile();
                System.out.println("Wrote current SongList with number of reentries");
            }
            else if ("pl".equals(control)) {
                System.out.println("Enter the lists to order divided by commas");
                String lists2 = sc.nextLine();
                String[] listArray = lists2.split(",");
                HashMap<String, Integer> listsAndLengths = new HashMap<>();
                for (String list2 : listArray) {
                    listsAndLengths.put(list2, getListLength(list2, list));
                }
                HashMap<String, Double> songAndPoints2 = new HashMap<>();
                for (Record r : list) {
                    int totalPoints = 0;
                    for (String list2 : listArray) {
                        Integer highestListValuePlusOne = listsAndLengths.get(list2) + 1;
                        Integer position = r.getPositionMap().get(list2);
                        if (nonNull(position)) {
                            totalPoints += (highestListValuePlusOne - position);
                        }
                    }
                    double finalPoints = (double) totalPoints / (double) listsAndLengths.size();
                    songAndPoints2.put(r.getArtist() + "|" + r.getTitle(), finalPoints);
                }
                SongList newList = new SongList();
                for (Map.Entry<String, Double> entry : songAndPoints2.entrySet()) {
                    String[] artistAndTitle = entry.getKey().split(Pattern.quote("|"));
                    Record r = new Record();
                    r.setArtist(artistAndTitle[0]);
                    r.setTitle(artistAndTitle[1]);
                    r.addPositionToMap("Points", entry.getValue().intValue());
                    double remainder = (entry.getValue() % 1) * 100;
                    r.addPositionToMap("Remainder", (int) remainder);
                    newList.add(r);
                }
                newList.outputToFile();
                System.out.println("Wrote current SongList calculated with number of points and remainder");
            }
            else if ("q".equals(control)) {
                takeInput = false;
            }
            else {
                System.out.println("Invalid input, try again");
            }
        }
    }

    private static int getListLength(String key, SongList list) {
        int currentMax = -1;
        for (Record r : list) {
            Integer position = r.getPositionMap().get(key);
            if (nonNull(position)) {
                if (currentMax < position) {
                    currentMax = position;
                }
            }
        }
        return currentMax;
    }
}
