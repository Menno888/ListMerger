import java.util.*;
import java.util.regex.Pattern;

import static java.util.Objects.nonNull;

public class ToplijstenMergerTools {

    private static final Scanner sc = new Scanner(System.in);

    public static void getTools(SongList list) {
        boolean takeInput = true;
        while(takeInput) {
            System.out.println("Stuff for tools");
            String control = sc.nextLine();
            switch (control) {
                case "hp":
                    for (Record song : list) {
                        System.out.print(song.showSong());
                        System.out.print(", highest position: ");
                        if (song.getPositionMap().size() > 0) {
                            int max = song.getPositionMap().values().stream().max(Comparator.naturalOrder()).get();
                            System.out.print(max);
                        }
                        else {
                            System.out.print("N/A");
                        }
                        System.out.println();
                    }
                    break;
                case "wp":
                    for (Record record : list) {
                        record.cleanPositionMap();
                    }
                    System.out.println("Done cleaning positions");
                    break;
                case "mt":
                    String testMatches = "The ";
                    for(int i = 0; i < list.size(); i++) {
                        if(list.get(i).getTitle().startsWith(testMatches)) {
                            for (Record record : list) {
                                if (record.getTitle().equals(list.get(i).getTitle().substring(testMatches.length()))) {
                                    System.out.println("Match found: " + list.get(i).showSong() + ", " + record.showSong());
                                }
                            }
                        }
                    }
                    System.out.println("--END FINDING MATCHES--");
                    break;
                case "re":
                    System.out.println("Give the lists separated by commas:");
                    String lists = sc.nextLine();
                    String[] reentryList = lists.split(",");
                    for (Record record : list) {
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
                        System.out.println(record.showSong() + ": " + total);
                    }
                case "to":
                    String[] toplijsten = {"R2NLA2016","R2NLA2015","R2NLA2014","R2NLA2013","R2NLA2012",
                            "R2NLA2011","R2NLA2010","R2NLA2009","R2NLA2008","R2NLA2007","R2NLA2006",
                            "R2NLA2005","R2NLA2004","R2NLA2003","R2NLA2002","R2NLA2001","R2NLA2000",
                            "R2NLA1999","VA2017","VA2016","VA2015","VA2014","VA2013","VA2012","VA2011",
                            "VA2010","VA2009","VA2008","VA2007","VA2006","VA2005","VA2004","VA2003",
                            "R10A2016","R10A2015","R10A2014","R10A2013","R10A2012","R10A2011","R10A2010",
                            "R10A2009","R10A2008","R10A2007","R10A2006","R10A2005","JA2017","JA2016",
                            "JA2015","JA2014","JA2013","JA2012","JA2011","JA2010","JA2009"};
                    double count2 = Arrays.stream(toplijsten).filter(e -> e.startsWith("R2NLA")).count();
                    double countV = Arrays.stream(toplijsten).filter(e -> e.startsWith("VA")).count();
                    double countG = Arrays.stream(toplijsten).filter(e -> e.startsWith("R10A")).count();
                    double countJ = Arrays.stream(toplijsten).filter(e -> e.startsWith("JA")).count();
                    LinkedHashMap<String, Double> songAndPoints = new LinkedHashMap<>();
                    for (Record currentRecord : list) {
                        double totaal2 = 0;
                        double totaalV = 0;
                        double totaalG = 0;
                        double totaalJ = 0;
                        for (Map.Entry<String, Integer> entry : currentRecord.getPositionMap().entrySet()) {
                            if (Arrays.asList(toplijsten).contains(entry.getKey())) {
                                if (entry.getKey().startsWith("R2NLA")) {
                                    if (entry.getValue() != null) {
                                        totaal2 = totaal2 + (2001 - entry.getValue());
                                    }
                                }
                                if (entry.getKey().startsWith("VA")) {
                                    if (entry.getValue() != null) {
                                        totaalV = totaalV + (1001 - entry.getValue());
                                    }
                                }
                                if (entry.getKey().startsWith("R10A")) {
                                    if (entry.getValue() != null) {
                                        totaalG = totaalG + (4001 - entry.getValue());
                                    }
                                }
                                if (entry.getKey().startsWith("JA")) {
                                    if (entry.getValue() != null) {
                                        totaalJ = totaalJ + (2001 - entry.getValue());
                                    }
                                }
                            }
                        }
                        double som = (totaal2 / count2) + (totaalV / countV) + (totaalG / (2 * countG)) + (totaalJ / countJ);
                        songAndPoints.put(currentRecord.showSong(), som);
                    }
                    for (Map.Entry<String, Double> song : songAndPoints.entrySet()) {
                        System.out.println(song.getKey() + ": " + song.getValue());
                    }
                case "pl":
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
                        LinkedHashMap<String, Integer> positions = new LinkedHashMap<>();
                        positions.put("Points", entry.getValue().intValue());
                        double remainder = (entry.getValue() % 1) * 100;
                        positions.put("Remainder", (int) remainder);
                        r.setPositionMap(positions);
                        newList.add(r);
                    }
                    list = newList;
                    break;
                case "q":
                    takeInput = false;
                    break;
                default:
                    System.out.println("Invalid input, try again");
                    break;
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
