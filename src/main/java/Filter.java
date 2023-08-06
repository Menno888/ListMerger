import java.util.*;

public class Filter {

    private static final Scanner sc = new Scanner(System.in);

    public static void filter(final SongList songList, final String lists) {
        final String[] toKeep = lists.split(",");
        final List<String> toKeepList = Arrays.asList(toKeep);
        final ArrayList<String> allEditions = songList.tagCheckup();
        final List<String> newList = new ArrayList<>();
        for (final String edition : allEditions) {
            if (toKeepList.contains(edition.substring(0, edition.length() - 4)) || toKeepList.contains(edition)) {
                newList.add(edition);
            }
        }
        String shouldAppearInAll = "n";
        String retainUnused = "n";
        System.out.println("Should appear [y] or not [n] appear in specified lists?");
        final String shouldAppear = sc.nextLine();
        if (newList.size() > 1 && "y".equals(shouldAppear)) {
            System.out.println("Should appear in all lists or not? [y/n]");
            shouldAppearInAll = sc.nextLine();
        }
        if ("y".equals(shouldAppear)) {
            System.out.println("Should retain lists that aren't in the selection?");
            retainUnused = sc.nextLine();
        }
        if ("y".equals(shouldAppear) && "n".equals(shouldAppearInAll)) {
            for (final Record currentRecord : songList) {
                boolean containsEntries = false;
                for (final Map.Entry<String, Integer> entry2 : currentRecord.getPositionMap().entrySet()) {
                    if (newList.contains(entry2.getKey())) {
                        containsEntries = true;
                        break;
                    }
                }
                if (!containsEntries) {
                    currentRecord.cleanPositionMap();
                }
            }
            songList.normalize();
            allEditions.removeAll(newList);
            if ("n".equals(retainUnused)) {
                for (final Record currentRecord : songList) {
                    for (final String edition : allEditions) {
                        currentRecord.getPositionMap().remove(edition);
                    }
                }
            }
        }
        else if ("y".equals(shouldAppear) && "y".equals(shouldAppearInAll)) {
            for (final Record currentRecord : songList) {
                int containsEntries = 0;
                for (final Map.Entry<String, Integer> entry2 : currentRecord.getPositionMap().entrySet()) {
                    if (newList.contains(entry2.getKey())) {
                        containsEntries++;
                    }
                }
                if (containsEntries != newList.size()) {
                    currentRecord.cleanPositionMap();
                }
            }
            songList.normalize();
            allEditions.removeAll(newList);
            if ("n".equals(retainUnused)) {
                for (final Record currentRecord : songList) {
                    for (final String edition : allEditions) {
                        currentRecord.getPositionMap().remove(edition);
                    }
                }
            }
        }
        else {
            for (final Record currentRecord : songList) {
                boolean containsEntries = false;
                for (final Map.Entry<String, Integer> entry2 : currentRecord.getPositionMap().entrySet()) {
                    if (newList.contains(entry2.getKey())) {
                        containsEntries = true;
                        break;
                    }
                }
                if (containsEntries) {
                    currentRecord.cleanPositionMap();
                }
            }
            songList.normalize();
        }
    }
}
