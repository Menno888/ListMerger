import java.util.*;

public class Filter {

    private static final Scanner sc = new Scanner(System.in);

    private static final String FILTER_OPTION_LISTS = "fl";
    private static final String FILTER_OPTION_YEARS = "fy";
    private static final String FILTER_OPTION_NO = "n";
    private static final String FILTER_OPTION_YES = "y";

    private static final String SEPARATOR_CHARACTER_COMMA = ",";

    private static List<String> newList = new ArrayList<>();

    public static void filter(final SongList songList, final String lists, final String filterOption) {
        if (FILTER_OPTION_LISTS.equals(filterOption)) {
            newList = getListsFromFilterStringLists(songList, lists);
        } else if (FILTER_OPTION_YEARS.equals(filterOption)) {
            newList = getListsFromFilterStringYears(songList, lists);
        }
        final List<String> allEditions = songList.tagCheckup();
        String shouldAppearInAll = FILTER_OPTION_NO;
        String retainUnused = FILTER_OPTION_NO;
        System.out.println("Should appear [y] or not [n] appear in specified lists?");
        final String shouldAppear = sc.nextLine();
        if (FILTER_OPTION_YES.equals(shouldAppear)) {
            if (newList.size() > 1) {
                System.out.println("Should appear in all lists or not? [y/n]");
                shouldAppearInAll = sc.nextLine();
            }
            System.out.println("Should retain lists that aren't in the selection?");
            retainUnused = sc.nextLine();
        }
        filterShouldAppear(songList, allEditions, shouldAppear, shouldAppearInAll, retainUnused);
    }

    private static void filterShouldAppear(final SongList songList, final List<String> allEditions, final String shouldAppear, final String shouldAppearInAll, final String retainUnused) {
        filterBasedOnAppearanceRules(songList, shouldAppear, shouldAppearInAll);
        if (FILTER_OPTION_YES.equals(shouldAppear)) {
            allEditions.removeAll(newList);
            if (FILTER_OPTION_NO.equals(retainUnused)) {
                removeAllUnusedListsFromSelection(songList, allEditions);
            }
        }
    }

    private static void filterBasedOnAppearanceRules(final SongList songList, final String shouldAppear, final String shouldAppearInAll) {
        for (final Song currentSong : songList) {
            int containsEntries = 0;
            for (final Map.Entry<String, Integer> entry2 : currentSong.getPositionMap().entrySet()) {
                if (newList.contains(entry2.getKey())) {
                    containsEntries++;
                }
            }
            if (shouldRemoveSongBasedOnNumberOfEntriesAndAppearanceRules(containsEntries, shouldAppear, shouldAppearInAll)) {
                currentSong.cleanPositionMap();
            }
        }
        songList.normalize();
    }

    private static boolean shouldRemoveSongBasedOnNumberOfEntriesAndAppearanceRules(final int numberOfEntries, final String shouldAppear, final String shouldAppearInAll) {
        if (FILTER_OPTION_YES.equals(shouldAppear)) {
            return FILTER_OPTION_YES.equals(shouldAppearInAll) && numberOfEntries != newList.size() || FILTER_OPTION_NO.equals(shouldAppearInAll) && numberOfEntries == 0;
        } else {
            return numberOfEntries > 0;
        }
    }

    private static List<String> getListsFromFilterStringLists(final SongList songList, final String lists) {
        final List<String> toKeepList = Arrays.asList(lists.split(SEPARATOR_CHARACTER_COMMA));
        final List<String> allEditions = songList.tagCheckup();
        final List<String> newList = new ArrayList<>();
        for (final String edition : allEditions) {
            if (Character.isDigit(edition.charAt(edition.length() - 1))) {
                if (toKeepList.contains(edition.substring(0, edition.length() - 4)) || toKeepList.contains(edition)) {
                    newList.add(edition);
                }
            } else {
                if (toKeepList.contains(edition.substring(0, edition.length() - 5)) || toKeepList.contains(edition)) {
                    newList.add(edition);
                }
            }
        }

        return newList;
    }

    private static List<String> getListsFromFilterStringYears(final SongList songList, final String lists) {
        final List<String> toKeepList = Arrays.asList(lists.split(SEPARATOR_CHARACTER_COMMA));
        final List<String> allEditions = songList.tagCheckup();
        final List<String> newList = new ArrayList<>();
        for (final String edition : allEditions) {
            if (Character.isDigit(edition.charAt(edition.length() - 1))) {
                if (toKeepList.contains(edition.substring(edition.length() - 4)) || toKeepList.contains(edition)) {
                    newList.add(edition);
                }
            } else {
                if (toKeepList.contains(edition.substring(edition.length() - 5, edition.length() - 1)) || toKeepList.contains(edition)) {
                    newList.add(edition);
                }
            }
        }

        return newList;
    }

    private static void removeAllUnusedListsFromSelection(final SongList songList, final List<String> allEditions) {
        for (final Song currentSong : songList) {
            for (final String edition : allEditions) {
                currentSong.getPositionMap().remove(edition);
            }
        }
    }
}
