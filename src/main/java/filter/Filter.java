package filter;

import dto.Song;
import dto.SongList;

import java.util.*;

import static tools.FieldUtil.*;

public class Filter {

    private static final Scanner sc = new Scanner(System.in);

    private static List<String> newList = new ArrayList<>();

    private Filter() {
        //No-args
    }

    public static void filter(final SongList songList, final String lists, final String filterOption) {
        newList = getListsFromFilterStringAndFilterOption(songList, lists, filterOption);
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

    private static List<String> getListsFromFilterStringAndFilterOption(final SongList songList, final String lists, final String filterOption) {
        final List<String> toKeepList = Arrays.asList(lists.split(SEPARATOR_CHARACTER_COMMA));
        final List<String> allEditions = songList.tagCheckup();
        final List<String> newList = new ArrayList<>();
        for (final String edition : allEditions) {
            if (shouldKeepListStringBasedOnEditionListAndFilterOption(edition, toKeepList, filterOption)) {
                newList.add(edition);
            }
        }

        return newList;
    }

    /*
    Matches a list abbreviations on whether it should, depending on the filter setting, for instance:
    LIST2020B, [LIST], "fl" -> true
    LIST1999, [NOPE], "fl" -> false
    LIST2000B, [1999,2000], "fy" -> true
    LIST2012, [2011,2013], "fy" -> false
     */
    private static boolean shouldKeepListStringBasedOnEditionListAndFilterOption(final String listToCheck, final List<String> filterLists, final String filterOption) {
        String listSubstring;
        boolean filterOnLists = "fl".equals(filterOption);
        boolean listEndsInYear = Character.isDigit(listToCheck.charAt(listToCheck.length() - 1));
        if (filterOnLists) {
            if (listEndsInYear) {
                listSubstring = listToCheck.substring(0, listToCheck.length() - 4);
            } else {
                listSubstring = listToCheck.substring(0, listToCheck.length() - 5);
            }
        } else {
            if (listEndsInYear) {
                listSubstring = listToCheck.substring(listToCheck.length() - 4);
            } else {
                listSubstring = listToCheck.substring(listToCheck.length() - 5, listToCheck.length() - 1);
            }
        }
        return filterLists.contains(listSubstring) || filterLists.contains(listToCheck);
    }

    private static void removeAllUnusedListsFromSelection(final SongList songList, final List<String> allEditions) {
        for (final Song currentSong : songList) {
            for (final String edition : allEditions) {
                currentSong.getPositionMap().remove(edition);
            }
        }
    }
}
