import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class ToplijstenMergerTools {

    private static final Scanner sc = new Scanner(System.in);

    public static void getTools(final SongList list) {
        boolean takeInput = true;
        while (takeInput) {
            System.out.println("Tools: (h)ighest (p)ositions, (l)owest (p)ositions, (w)ithout (p)ositions, count (re)entries, (p)oint (l)ist, (q)uit, (y)early (e)xtremes, (f)ilter (a)rtist, (f)ilter (t)itle");
            final String control = sc.nextLine();
            if ("hp".equals(control)) {
                final SongList newList = new SongList();
                for (final Record song : list) {
                    final Record r = new Record();
                    r.setArtist(song.getArtist() + " - " + song.getTitle());
                    final int numberOfEntries = song.getPositionMap().size();
                    if (numberOfEntries > 0) {
                        int currentMax = 100000;
                        StringBuilder listStringWithCommas = new StringBuilder();
                        for (final Map.Entry<String, Integer> keyAndValue : song.getPositionMap().entrySet()) {
                            if (keyAndValue.getValue() < currentMax) {
                                currentMax = keyAndValue.getValue();
                                listStringWithCommas = new StringBuilder(keyAndValue.getKey());
                            } else if (keyAndValue.getValue() == currentMax) {
                                listStringWithCommas.append(",").append(keyAndValue.getKey());
                            }
                        }
                        r.setTitle(listStringWithCommas.toString());
                        r.addPositionToMap("MAX", currentMax);
                    }
                    else {
                        r.setTitle("NONE");
                        r.addPositionToMap("MAX", 0);
                    }
                    r.addPositionToMap("POS", numberOfEntries);
                    newList.add(r);
                }
                newList.outputToFile();
                System.out.println("Wrote current SongList with highest positions");
            }
            else if ("lp".equals(control)) {
                final SongList newList = new SongList();
                for (final Record song : list) {
                    final Record r = new Record();
                    r.setArtist(song.getArtist() + " - " + song.getTitle());
                    final int numberOfEntries = song.getPositionMap().size();
                    if (numberOfEntries > 0) {
                        int currentMin = 0;
                        StringBuilder listStringWithCommas = new StringBuilder();
                        for (final Map.Entry<String, Integer> keyAndValue : song.getPositionMap().entrySet()) {
                            if (keyAndValue.getValue() > currentMin) {
                                currentMin = keyAndValue.getValue();
                                listStringWithCommas = new StringBuilder(keyAndValue.getKey());
                            } else if (keyAndValue.getValue() == currentMin) {
                                listStringWithCommas.append(",").append(keyAndValue.getKey());
                            }
                        }
                        r.setTitle(listStringWithCommas.toString());
                        r.addPositionToMap("MIN", currentMin);
                    }
                    else {
                        r.setTitle("NONE");
                        r.addPositionToMap("MIN", 0);
                    }
                    r.addPositionToMap("POS", numberOfEntries);
                    newList.add(r);
                }
                newList.outputToFile();
                System.out.println("Wrote current SongList with lowest positions");
            }
            else if ("wp".equals(control)) {
                for (final Record r : list) {
                    r.cleanPositionMap();
                }
                System.out.println("Done cleaning positions");
            }
            else if ("re".equals(control)) {
                System.out.println("Give the lists separated by commas:");
                final String lists = sc.nextLine();
                final SongList newList = new SongList();
                final String[] reentryList = lists.split(",");
                for (final Record record : list) {
                    final Record r = new Record();
                    r.setArtist(record.getArtist());
                    r.setTitle(record.getTitle());
                    int total = 0;
                    for (int j = 0; j < reentryList.length - 1; j++) {
                        final Integer p1 = record.getPositionMap().get(reentryList[j]);
                        final Integer p2 = record.getPositionMap().get(reentryList[j + 1]);
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
                System.out.println("Enter the lists to order divided by commas (or leave blank for all lists in selection)");
                final String lists2 = sc.nextLine();
                String[] listArray = new String[2000];
                final HashMap<String, Integer> listsAndLengths = new HashMap<>();
                if (!"".equals(lists2)) {
                    listArray = lists2.split(",");
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
                for (final Record r : list) {
                    int totalPoints = 0;
                    for (final String list2 : listArray) {
                        final Integer highestListValuePlusOne = listsAndLengths.get(list2) + 1;
                        final Integer position = r.getPositionMap().get(list2);
                        if (nonNull(position)) {
                            totalPoints += (highestListValuePlusOne - position);
                        }
                    }
                    double finalPoints = (double) totalPoints / (double) listsAndLengths.size();
                    songAndPoints2.put(r.getArtist() + "|" + r.getTitle(), finalPoints);
                }
                final SongList newList = new SongList();
                for (final Map.Entry<String, Double> entry : songAndPoints2.entrySet()) {
                    final String[] artistAndTitle = entry.getKey().split(Pattern.quote("|"));
                    final Record r = new Record();
                    r.setArtist(artistAndTitle[0]);
                    r.setTitle(artistAndTitle[1]);
                    r.addPositionToMap("Points", entry.getValue().intValue());
                    final double remainder = (entry.getValue() % 1) * 100;
                    r.addPositionToMap("Remainder", (int) remainder);
                    newList.add(r);
                }
                newList.outputToFile();
                System.out.println("Wrote current SongList calculated with number of points and remainder");
            }
            else if ("ye".equals(control)) {
                final SongList newList = new SongList();
                System.out.println("Enter two year numbers separated by a comma for which you want to generate the diff list (Warning! Can take ~10m)");
                final String yearString = sc.nextLine();
                final String[] yearStringArray = yearString.split(",");
                final int yearTo = Math.max(Integer.parseInt(yearStringArray[0]), Integer.parseInt(yearStringArray[1]));
                final int yearFrom = Math.min(Integer.parseInt(yearStringArray[0]), Integer.parseInt(yearStringArray[1]));
                final Set<String> allListsAbbreviations = getAllListAbbreviations(list);
                for (final Record r : list) {
                    final Record copy = new Record(r.getArtist(), r.getTitle());
                    if (inAnyListThisOrPreviousYear(r, yearTo, yearFrom)) {
                        final double averageClimb = getAverageClimbForRecordComparedToYear(r, allListsAbbreviations, yearTo, yearFrom, list);
                        copy.addPositionToMap("Percentage", (int) averageClimb);
                        final double remainder = (averageClimb % 1) * 1000;
                        copy.addPositionToMap("Remainder", (int) remainder);
                        System.out.println("ADD - SongName: " + r.showSong());
                    } else {
                        copy.addPositionToMap("Percentage", 0);
                        copy.addPositionToMap("Remainder", 0);
                        System.out.println("SKIP - SongName: " + r.showSong());
                    }
                    newList.add(copy);
                }
                newList.outputToFile();
                System.out.println("Wrote current SongList calculated with average climb/drop and remainder");
            }
            else if ("fa".equals(control)) {
                System.out.println("Enter the words to filter on");
                final String filterString = sc.nextLine();
                SongList newList = list
                        .stream()
                        .filter(e -> e.getArtist().toLowerCase().contains(filterString.toLowerCase()))
                        .collect(Collectors.toCollection(SongList::new));
                newList.outputToFile();
                System.out.println("Wrote current SongList filtered on artist");
            }
            else if ("ft".equals(control)) {
                System.out.println("Enter the words to filter on");
                final String filterString = sc.nextLine();
                SongList newList = list
                        .stream()
                        .filter(e -> e.getTitle().toLowerCase().contains(filterString.toLowerCase()))
                        .collect(Collectors.toCollection(SongList::new));
                newList.outputToFile();
                System.out.println("Wrote current SongList filtered on title");
            }
            else if ("q".equals(control)) {
                takeInput = false;
            }
            else {
                System.out.println("Invalid input, try again");
            }
        }
    }

    private static double getAverageClimbForRecordComparedToYear(final Record record, final Set<String> allListAbbreviations, final int yearTo, final int yearFrom, final SongList list) {
        final List<Double> climbsAndDrops = new ArrayList<>();
        for (final String abbr : allListAbbreviations) {
            final String abbrFirstPart = abbr.substring(0, abbr.length() - 4);
            final String abbrSecondPart = abbr.substring(abbr.length() - 4);
            if (String.valueOf(yearTo).equals(abbrSecondPart) && allListAbbreviations.contains(abbrFirstPart + yearFrom)) {

                final String currentYear = abbrFirstPart + yearTo;
                final String prevYear = abbrFirstPart + yearFrom;
                final int currentYearLength = getListLength(currentYear, list);
                final int prevYearLength = getListLength(prevYear, list);

                final HashMap<String, Integer> positionMap = record.getPositionMap();
                double climbOrDrop;
                if (positionMap.containsKey(currentYear) && positionMap.containsKey(prevYear)) {
                    climbOrDrop = ((double) positionMap.get(prevYear) - (double) positionMap.get(currentYear)) / (double) Math.max(currentYearLength + 1, prevYearLength + 1);
                } else if (positionMap.containsKey(currentYear) && !positionMap.containsKey(prevYear)) {
                    climbOrDrop = ((double) prevYearLength + 1 - (double) positionMap.get(currentYear)) / (double) Math.max(currentYearLength + 1, prevYearLength + 1);
                } else if (!positionMap.containsKey(currentYear) && positionMap.containsKey(prevYear)) {
                    climbOrDrop = ((double) positionMap.get(prevYear) - (double) currentYearLength + 1) / (double) Math.max(currentYearLength + 1, prevYearLength + 1);
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
        for (final Record r : list) {
            final Integer position = r.getPositionMap().get(key);
            if (nonNull(position)) {
                if (currentMax < position) {
                    currentMax = position;
                }
            }
        }
        return currentMax;
    }

    private static Set<String> getAllListAbbreviations(final SongList list) {
        final Set<String> lists = new HashSet<>();
        for (final Record r : list) {
            lists.addAll(r.getPositionMap().keySet());
        }
        return lists;
    }

    private static boolean inAnyListThisOrPreviousYear(final Record record, final int yearTo, final int yearFrom) {
        final LinkedHashMap<String, Integer> positionMap = record.getPositionMap();
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
