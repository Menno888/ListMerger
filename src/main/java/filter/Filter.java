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
                currentSong.clearPositionMap();
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
        final String[] toKeepList = lists.split(SEPARATOR_CHARACTER_COMMA);
        final List<String> allEditions = songList.tagCheckup();
        final List<String> newList = new ArrayList<>();
        for (final String edition : allEditions) {
            boolean hasMatch = false;
            for (final String filterSingleList : toKeepList) {
                if (listAbbreviationIsSubstringOfFullListAbbreviation(edition, filterSingleList, filterOption)) {
                    hasMatch = true;
                    break;
                }
            }
            if (hasMatch) {
                newList.add(edition);
            }
        }

        return newList;
    }

    /*
    Matches a list abbreviations on whether it should be added, counting lists with appendices only when searching for one source or a year
    VRNCA2003, VRNCA, "fl" -> true (matches source and category)
    VRNCA2003B, VRNCA, "fl" -> false (matches source and category, but isn't considered part of the series)
    VRNCA2003B, VRNC, "fl" -> true (matches source, every abbreviation starting with the filter counts)
    JOBEA2003, VRNCA, "fl" -> false (doesn't match source and category)
    R2NLA1999, 1999, "fy" -> true (matches year)
    VRNCA2003B, 2003, "fy" -> true (matches year, every abbreviation containing it at the end counts)
    R2NLA1999, 2000, "fy" -> false (doesn't match year)
     */
    private static boolean listAbbreviationIsSubstringOfFullListAbbreviation(final String listToCheck, final String filterSingleList, final String filterOption) {
        boolean filterOnLists = "fl".equals(filterOption);
        final boolean abbreviationEndsInAppendix = listAbbreviationEndsInLetter(listToCheck);
        final int offset = abbreviationEndsInAppendix ? 1 : 0;
        if (abbreviationEndsInAppendix && filterOnLists) {
            return listToCheck.equals(filterSingleList) || filterSingleList.length() == 4;
        }
        if (filterOnLists) {
            final String listToCheckWithoutYear = listToCheck.substring(0, listToCheck.length() - 4);
            if (!listAbbreviationEndsInLetter(filterSingleList) || filterSingleList.length() == 4) {
                return listToCheck.startsWith(filterSingleList);
            } else {
                return listToCheckWithoutYear.equals(filterSingleList);
            }
        } else {
            return listToCheck.substring(listToCheck.length() - (4 + offset), listToCheck.length() - offset).equals(filterSingleList);
        }
    }

    private static boolean listAbbreviationEndsInLetter(final String listAbbreviation) {
        return !Character.isDigit(listAbbreviation.charAt(listAbbreviation.length() - 1));
    }

    private static void removeAllUnusedListsFromSelection(final SongList songList, final List<String> allEditions) {
        for (final Song currentSong : songList) {
            for (final String edition : allEditions) {
                currentSong.getPositionMap().remove(edition);
            }
        }
    }
}
