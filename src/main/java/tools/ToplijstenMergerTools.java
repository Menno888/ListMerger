package tools;

import dto.Song;
import dto.SongList;
import merger.Merger;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class ToplijstenMergerTools {

    private static final Scanner sc = new Scanner(System.in);

    private static final String COLUMN_HEADER_MINIMUM = "MIN";
    private static final String COLUMN_HEADER_MAXIMUM = "MAX";
    private static final String COLUMN_HEADER_POSITION = "POS";
    private static final String COLUMN_HEADER_COUNT = "NUM";
    private static final String COLUMN_HEADER_POINTS = "POINTS";
    private static final String COLUMN_HEADER_AVERAGE = "AVERAGE";
    private static final String COLUMN_HEADER_PERCENTAGE = "PERCENTAGE";
    private static final String COLUMN_HEADER_REMAINDER = "REMAINDER";
    private static final String COLUMN_HEADER_NONE = "NONE";

    private static final String SEPARATOR_CHARACTER_COMMA = ",";
    private static final String SEPARATOR_CHARACTER_PIPE = "|";
    private static final String SEPARATOR_CHARACTER_HYPHEN_SPACED = " - ";

    private static boolean takeInput = true;

    private ToplijstenMergerTools() {
        //No-args
    }

    public static void getTools(final SongList list) {
        while (takeInput) {
            System.out.println("Tools: (h)ighest (p)ositions, (l)owest (p)ositions, (w)ithout (p)ositions, count (re)entries, (p)oint (l)ist, (q)uit, (y)early (e)xtremes, (f)ilter (a)rtist, (f)ilter (t)itle, (a)verage (a)ppearances, (d)iff (l)ist l/r");
            final String control = sc.nextLine();
            switch (control) {
                case "hp" -> outputHighestPositionsForSongs(list);
                case "lp" -> outputLowestPositionsForSongs(list);
                case "wp" -> cleanPositionMap(list);
                case "re" -> outputReentriesForSongs(list);
                case "pl" -> outputPointListForSongs(list);
                case "ye" -> outputDiffListPerYearForSongs(list);
                case "fa" -> filterOnArtist(list);
                case "ft" -> filterOnTitle(list);
                case "aa" -> outputAveragesAndNumberOfAppearancesForSongs(list);
                case "dl" -> outputTwoDiffListsForSongs();
                case "q" -> takeInput = false;
                default -> System.out.println("Invalid input, try again");
            }
        }
    }

    private static void outputHighestPositionsForSongs(final SongList list) {
        final SongList newList = new SongList();
        for (final Song song : list) {
            final Song r = new Song();
            r.setArtist(song.getArtist() + SEPARATOR_CHARACTER_HYPHEN_SPACED + song.getTitle());
            final int numberOfEntries = song.getPositionMap().size();
            if (numberOfEntries > 0) {
                int currentMax = Integer.MAX_VALUE;
                StringBuilder listStringWithCommas = new StringBuilder();
                for (final Map.Entry<String, Integer> keyAndValue : song.getPositionMap().entrySet()) {
                    if (keyAndValue.getValue() < currentMax) {
                        currentMax = keyAndValue.getValue();
                        listStringWithCommas = new StringBuilder(keyAndValue.getKey());
                    } else if (keyAndValue.getValue() == currentMax) {
                        listStringWithCommas.append(SEPARATOR_CHARACTER_COMMA).append(keyAndValue.getKey());
                    }
                }
                r.setTitle(listStringWithCommas.toString());
                r.addPositionToMap(COLUMN_HEADER_MAXIMUM, currentMax);
            }
            else {
                r.setTitle(COLUMN_HEADER_NONE);
                r.addPositionToMap(COLUMN_HEADER_MAXIMUM, 0);
            }
            r.addPositionToMap(COLUMN_HEADER_POSITION, numberOfEntries);
            newList.add(r);
        }
        newList.outputToFile();
        System.out.println("Wrote current dto.SongList with highest positions");
    }

    private static void outputLowestPositionsForSongs(final SongList list) {
        final SongList newList = new SongList();
        for (final Song song : list) {
            final Song r = new Song();
            r.setArtist(song.getArtist() + SEPARATOR_CHARACTER_HYPHEN_SPACED + song.getTitle());
            final int numberOfEntries = song.getPositionMap().size();
            if (numberOfEntries > 0) {
                int currentMin = -1;
                StringBuilder listStringWithCommas = new StringBuilder();
                for (final Map.Entry<String, Integer> keyAndValue : song.getPositionMap().entrySet()) {
                    if (keyAndValue.getValue() > currentMin) {
                        currentMin = keyAndValue.getValue();
                        listStringWithCommas = new StringBuilder(keyAndValue.getKey());
                    } else if (keyAndValue.getValue() == currentMin) {
                        listStringWithCommas.append(SEPARATOR_CHARACTER_COMMA).append(keyAndValue.getKey());
                    }
                }
                r.setTitle(listStringWithCommas.toString());
                r.addPositionToMap(COLUMN_HEADER_MINIMUM, currentMin);
            }
            else {
                r.setTitle(COLUMN_HEADER_NONE);
                r.addPositionToMap(COLUMN_HEADER_MINIMUM, 0);
            }
            r.addPositionToMap(COLUMN_HEADER_POSITION, numberOfEntries);
            newList.add(r);
        }
        newList.outputToFile();
        System.out.println("Wrote current dto.SongList with lowest positions");
    }

    private static void cleanPositionMap(final SongList list) {
        for (final Song r : list) {
            r.cleanPositionMap();
        }
        System.out.println("Done cleaning positions");
    }

    private static void outputReentriesForSongs(final SongList list) {
        System.out.println("Give the lists separated by commas:");
        final String lists = sc.nextLine();
        final SongList newList = new SongList();
        final String[] reentryList = lists.split(SEPARATOR_CHARACTER_COMMA);
        for (final Song song : list) {
            final Song r = new Song();
            r.setArtist(song.getArtist());
            r.setTitle(song.getTitle());
            int total = 0;
            for (int j = 0; j < reentryList.length - 1; j++) {
                final Integer p1 = song.getPositionMap().get(reentryList[j]);
                final Integer p2 = song.getPositionMap().get(reentryList[j + 1]);
                if ((p1 != null) && (p2 == null || p2 == 0)) {
                    total++;
                }
            }
            if (song.getPositionMap().get(reentryList[reentryList.length - 1]) != null) {
                total++;
            }
            r.addPositionToMap(COLUMN_HEADER_COUNT, total);
            newList.add(r);
        }
        newList.outputToFile();
        System.out.println("Wrote current dto.SongList with number of reentries");
    }

    private static void outputPointListForSongs(final SongList list) {
        System.out.println("Enter the lists to order divided by commas (or leave blank for all lists in selection)");
        final String lists2 = sc.nextLine();
        String[] listArray = new String[2000];
        final HashMap<String, Integer> listsAndLengths = new HashMap<>();
        if (!"".equals(lists2)) {
            listArray = lists2.split(SEPARATOR_CHARACTER_COMMA);
            for (final String list2 : listArray) {
                listsAndLengths.put(list2, getListLength(list2, list));
            }
        }
        else {
            final Set<String> abbrSet = getAllListAbbreviations(list);
            int arrayIndex = 0;
            for (final String l : abbrSet) {
                listArray[arrayIndex] = l;
                arrayIndex++;
            }
            for (final String list2 : listArray) {
                listsAndLengths.put(list2, getListLength(list2, list));
            }
        }
        final HashMap<String, Double> songAndPoints2 = new HashMap<>();
        for (final Song r : list) {
            int totalPoints = 0;
            for (final String list2 : listArray) {
                final Integer highestListValuePlusOne = listsAndLengths.get(list2) + 1;
                final Integer position = r.getPositionMap().get(list2);
                if (nonNull(position)) {
                    totalPoints += (highestListValuePlusOne - position);
                }
            }
            double finalPoints = (double) totalPoints / (double) listsAndLengths.size();
            songAndPoints2.put(r.getArtist() + SEPARATOR_CHARACTER_PIPE + r.getTitle(), finalPoints);
        }
        final SongList newList = new SongList();
        for (final Map.Entry<String, Double> entry : songAndPoints2.entrySet()) {
            final String[] artistAndTitle = entry.getKey().split(Pattern.quote(SEPARATOR_CHARACTER_PIPE));
            final Song r = new Song();
            r.setArtist(artistAndTitle[0]);
            r.setTitle(artistAndTitle[1]);
            r.addPositionToMap(COLUMN_HEADER_POINTS, entry.getValue().intValue());
            r.addPositionToMap(COLUMN_HEADER_REMAINDER, getRemainderFromDoubleWithNumberOfPrecision(entry.getValue(), 2));
            newList.add(r);
        }
        newList.outputToFile();
        System.out.println("Wrote current dto.SongList calculated with number of points and remainder");
    }

    private static void outputDiffListPerYearForSongs(final SongList list) {
        final SongList newList = new SongList();
        System.out.println("Enter two year numbers separated by a comma for which you want to generate the diff list (Warning! Can take ~10m)");
        final String yearString = sc.nextLine();
        final String[] yearStringArray = yearString.split(SEPARATOR_CHARACTER_COMMA);
        final int yearTo = Math.max(Integer.parseInt(yearStringArray[0]), Integer.parseInt(yearStringArray[1]));
        final int yearFrom = Math.min(Integer.parseInt(yearStringArray[0]), Integer.parseInt(yearStringArray[1]));
        final Set<String> allListsAbbreviations = getAllListAbbreviations(list);
        for (final Song r : list) {
            final Song copy = new Song(r.getArtist(), r.getTitle());
            if (inAnyListThisOrPreviousYear(r, yearTo, yearFrom)) {
                final double averageClimb = getAverageClimbForSongComparedToYear(r, allListsAbbreviations, yearTo, yearFrom, list);
                copy.addPositionToMap(COLUMN_HEADER_PERCENTAGE, (int) averageClimb);
                copy.addPositionToMap(COLUMN_HEADER_REMAINDER, getRemainderFromDoubleWithNumberOfPrecision(averageClimb, 3));
                System.out.println("ADD - SongName: " + r.showSong());
            } else {
                copy.addPositionToMap(COLUMN_HEADER_PERCENTAGE, 0);
                copy.addPositionToMap(COLUMN_HEADER_REMAINDER, 0);
                System.out.println("SKIP - SongName: " + r.showSong());
            }
            newList.add(copy);
        }
        newList.outputToFile();
        System.out.println("Wrote current dto.SongList calculated with average climb/drop and remainder");
    }

    private static void filterOnArtist(final SongList list) {
        System.out.println("Enter the words to filter on");
        final String filterString = sc.nextLine();
        final SongList newList = list
                .stream()
                .filter(e -> e.getArtist().toLowerCase().contains(filterString.toLowerCase()))
                .collect(Collectors.toCollection(SongList::new));
        newList.outputToFile();
        System.out.println("Wrote current dto.SongList filtered on artist");
    }

    private static void filterOnTitle(final SongList list) {
        System.out.println("Enter the words to filter on");
        final String filterString = sc.nextLine();
        final SongList newList = list
                .stream()
                .filter(e -> e.getTitle().toLowerCase().contains(filterString.toLowerCase()))
                .collect(Collectors.toCollection(SongList::new));
        newList.outputToFile();
        System.out.println("Wrote current dto.SongList filtered on title");
    }

    private static void outputAveragesAndNumberOfAppearancesForSongs(final SongList list) {
        final SongList newList = new SongList();
        for (final Song r : list) {
            final Song newSong = new Song();
            final int numberOfAppearances = r.getPositionMap().size();
            final int sumOfAppearances = r.getPositionMap().values().stream().mapToInt(Integer::intValue).sum();
            final double averageDouble = ((double) sumOfAppearances / (double) numberOfAppearances);
            final int average = (int) averageDouble;
            newSong.setArtist(r.getArtist());
            newSong.setTitle(r.getTitle());
            newSong.addPositionToMap(COLUMN_HEADER_COUNT, numberOfAppearances);
            newSong.addPositionToMap(COLUMN_HEADER_AVERAGE, average);
            newSong.addPositionToMap(COLUMN_HEADER_REMAINDER, getRemainderFromDoubleWithNumberOfPrecision(averageDouble, 2));
            newList.add(newSong);
        }
        newList.outputToFile();
        System.out.println("Wrote current dto.SongList with number of appearances and average position");
    }

    private static void outputTwoDiffListsForSongs() {
        System.out.println("Enter the two lists to find the diff on separated by a comma");
        final String listString = sc.nextLine();
        final String[] listArray = listString.split(SEPARATOR_CHARACTER_COMMA);
        final SongList newListLeft = new SongList();
        Merger.merge(newListLeft, listArray[0]);
        final SongList newListRight = new SongList();
        Merger.merge(newListRight, listArray[1]);
        final SongList newListLeftResult = new SongList();
        final SongList newListRightResult = new SongList();
        for (Song r : newListLeft) {
            r.cleanPositionMap();
            if (!newListRight.contains(r)) {
                newListLeftResult.add(r);
            }
        }
        for (Song r : newListRight) {
            r.cleanPositionMap();
            if (!newListLeft.contains(r)) {
                newListRightResult.add(r);
            }
        }
        newListLeftResult.outputToFile(listArray[0] + "leftdiff");
        newListRightResult.outputToFile(listArray[1] + "rightdiff");
        System.out.println("Wrote two lists containing diffs");
    }

    private static int getRemainderFromDoubleWithNumberOfPrecision(final double doubleValue, final int precision) {
        return (int) ((doubleValue % 1) * Math.pow(10, Math.abs(precision)));
    }

    private static double getAverageClimbForSongComparedToYear(final Song song, final Set<String> allListAbbreviations, final int yearTo, final int yearFrom, final SongList list) {
        final List<Double> climbsAndDrops = new ArrayList<>();
        for (final String abbr : allListAbbreviations) {
            final String abbrFirstPart = abbr.substring(0, abbr.length() - 4);
            final String abbrSecondPart = abbr.substring(abbr.length() - 4);
            if (String.valueOf(yearTo).equals(abbrSecondPart) && allListAbbreviations.contains(abbrFirstPart + yearFrom)) {

                final String currentYear = abbrFirstPart + yearTo;
                final String prevYear = abbrFirstPart + yearFrom;
                final int currentYearLength = getListLength(currentYear, list);
                final int prevYearLength = getListLength(prevYear, list);

                final Map<String, Integer> positionMap = song.getPositionMap();
                double climbOrDrop;
                if (positionMap.containsKey(currentYear) && positionMap.containsKey(prevYear)) {
                    climbOrDrop = ((double) positionMap.get(prevYear) - (double) positionMap.get(currentYear)) / Math.max(currentYearLength + 1, prevYearLength + 1);
                } else if (positionMap.containsKey(currentYear) && !positionMap.containsKey(prevYear)) {
                    climbOrDrop = ((double) prevYearLength + 1 - (double) positionMap.get(currentYear)) / Math.max(currentYearLength + 1, prevYearLength + 1);
                } else if (!positionMap.containsKey(currentYear) && positionMap.containsKey(prevYear)) {
                    climbOrDrop = ((double) positionMap.get(prevYear) - (double) currentYearLength + 1) / Math.max(currentYearLength + 1, prevYearLength + 1);
                } else {
                    climbOrDrop = 0.0;
                }
                climbsAndDrops.add(climbOrDrop * 100);
            }
        }

        return climbsAndDrops
                .stream()
                .mapToDouble(e -> e)
                .average()
                .orElse(0.0);
    }

    private static int getListLength(final String key, final SongList list) {
        int currentMax = -1;
        for (final Song r : list) {
            final Integer position = r.getPositionMap().get(key);
            if (nonNull(position) && currentMax < position) {
                currentMax = position;
            }
        }
        return currentMax;
    }

    private static Set<String> getAllListAbbreviations(final SongList list) {
        final Set<String> lists = new HashSet<>();
        for (final Song r : list) {
            lists.addAll(r.getPositionMap().keySet());
        }
        return lists;
    }

    private static boolean inAnyListThisOrPreviousYear(final Song song, final int yearTo, final int yearFrom) {
        final Map<String, Integer> positionMap = song.getPositionMap();
        boolean wasInAList = false;
        for (final String key : positionMap.keySet()) {
            final String listAbbrYearPart = key.substring(key.length() - 4);
            if (String.valueOf(yearTo).equals(listAbbrYearPart) || String.valueOf(yearFrom).equals(listAbbrYearPart)) {
                wasInAList = true;
                break;
            }
        }
        return wasInAList;
    }
}
