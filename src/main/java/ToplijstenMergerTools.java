import java.util.*;
import java.util.regex.Pattern;

import static java.util.Objects.nonNull;

public class ToplijstenMergerTools {

    private static final Scanner sc = new Scanner(System.in);

    public static void getTools(SongList list) {
        boolean takeInput = true;
        while(takeInput) {
            System.out.println("Tools: (h)ighest (p)ositions, (l)owest (p)ositions, (w)ithout (p)ositions, count (re)entries, (p)oint (l)ist, (q)uit, (y)early (e)xtremes");
            String control = sc.nextLine();
            if ("hp".equals(control)) {
                SongList newList = new SongList();
                for (Record song : list) {
                    Record r = new Record();
                    r.setArtist(song.getArtist() + " - " + song.getTitle());
                    int numberOfEntries = song.getPositionMap().size();
                    if (numberOfEntries > 0) {
                        int currentMax = 100000;
                        StringBuilder listStringWithCommas = new StringBuilder();
                        for (Map.Entry<String, Integer> keyAndValue : song.getPositionMap().entrySet()) {
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
                SongList newList = new SongList();
                for (Record song : list) {
                    Record r = new Record();
                    r.setArtist(song.getArtist() + " - " + song.getTitle());
                    int numberOfEntries = song.getPositionMap().size();
                    if (numberOfEntries > 0) {
                        int currentMin = 0;
                        StringBuilder listStringWithCommas = new StringBuilder();
                        for (Map.Entry<String, Integer> keyAndValue : song.getPositionMap().entrySet()) {
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
            else if ("ye".equals(control)) {
                SongList newList = new SongList();
                System.out.println("Enter two year numbers separated by a comma for which you want to generate the diff list (Warning! Can take ~10m)");
                String yearString = sc.nextLine();
                String[] yearStringArray = yearString.split(",");
                int yearTo = Math.max(Integer.parseInt(yearStringArray[0]), Integer.parseInt(yearStringArray[1]));
                int yearFrom = Math.min(Integer.parseInt(yearStringArray[0]), Integer.parseInt(yearStringArray[1]));
                Set<String> allListsAbbreviations = getAllListAbbreviations(list);
                for (Record r : list) {
                    Record copy = new Record(r.getArtist(), r.getTitle());
                    if (inAnyListThisOrPreviousYear(r, yearTo, yearFrom)) {
                        double averageClimb = getAverageClimbForRecordComparedToYear(r, allListsAbbreviations, yearTo, yearFrom, list);
                        copy.addPositionToMap("Percentage", (int) averageClimb);
                        double remainder = (averageClimb % 1) * 1000;
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
            else if ("q".equals(control)) {
                takeInput = false;
            }
            else {
                System.out.println("Invalid input, try again");
            }
        }
    }

    private static double getAverageClimbForRecordComparedToYear(Record record, Set<String> allListAbbreviations, int yearTo, int yearFrom, SongList list) {
        List<Double> climbsAndDrops = new ArrayList<>();
        for (String abbr : allListAbbreviations) {
            String abbrFirstPart = abbr.substring(0, abbr.length() - 4);
            String abbrSecondPart = abbr.substring(abbr.length() - 4);
            if (String.valueOf(yearTo).equals(abbrSecondPart) && allListAbbreviations.contains(abbrFirstPart + yearFrom)) {

                String currentYear = abbrFirstPart + yearTo;
                String prevYear = abbrFirstPart + yearFrom;
                int currentYearLength = getListLength(currentYear, list);
                int prevYearLength = getListLength(prevYear, list);

                HashMap<String, Integer> positionMap = record.getPositionMap();
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

    private static Set<String> getAllListAbbreviations(SongList list) {
        Set<String> lists = new HashSet<>();
        for (Record r : list) {
            lists.addAll(r.getPositionMap().keySet());
        }
        return lists;
    }

    private static boolean inAnyListThisOrPreviousYear(Record record, int yearTo, int yearFrom) {
        LinkedHashMap<String, Integer> positionMap = record.getPositionMap();
        boolean wasInAList = false;
        for (String key : positionMap.keySet()) {
            String listAbbrYearPart = key.substring(key.length() - 4);
            if (String.valueOf(yearTo).equals(listAbbrYearPart) || String.valueOf(yearFrom).equals(listAbbrYearPart)) {
                wasInAList = true;
                break;
            }
        }
        return wasInAList;
    }
}
