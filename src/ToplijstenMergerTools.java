import java.util.ArrayList;
import java.util.Scanner;

public class ToplijstenMergerTools {

    private static boolean takeInput = true;
    private static Scanner sc = new Scanner(System.in);

    public void getTools(ArrayList<Record> list) {
        while(takeInput) {
            System.out.println("Stuff for tools");
            String control = sc.nextLine();
            if(control.equals("option1")) {
                for (Record song : list) {
                    System.out.print(song.showSong());
                    System.out.print(", highest position: ");
                    int max = song.getPositionMap().entrySet().stream().map(e -> (int) e.getValue()).min(Integer::min).get();
                    System.out.print(max);
                    System.out.println();
                }
            }
            else if(control.equals("option2")) {
                System.out.println("Nothing too interesting");
            }
        }
    }
}

/*
*****Strip songList from keys and values for extended Excel base*****
                ArrayList<Record> newList = new ArrayList<Record>(50000);
                for(int i = 0; i < songList.size(); i++) {
                    Record r = new Record();
                    r.setArtiest(songList.get(i).getArtiest());
                    r.setNummer(songList.get(i).getNummer());
                    newList.add(r);
                }
                songList = newList;
 */

/*
*****Test matching titles without "The/De"
                String testMatches = "The "
                for(int i = 0; i < songList.size(); i++) {
                    if(songList.get(i).getNummer().startsWith(testMatches)) {
                        for(int j = 0; j < songList.size(); j++) {
                            if(songList.get(j).getNummer().equals(songList.get(i).getNummer().substring(testMatches.length()))) {
                                System.out.println("Match found: " + songList.get(i).showSong() + ", " + songList.get(j).showSong());
                            }
                        }
                    }
                }
 */

/*
*****Highest positions for every song
                for(int i = 0; i < songList.size(); i++) {
                    System.out.print(songList.get(i).showSong());
                    System.out.print(", highest position: ");
                    int max = songList.get(i).getPositionMap().entrySet().stream().map(e -> e.getValue()).min(Integer::min).get();
                    System.out.print(max);
                    System.out.println();
                }
 */

/*
*****Show number of appearances
                for(int i = 0; i < songList.size(); i++) {
                    System.out.println(songList.get(i).showSong() + ", " + songList.get(i).getAppearances());
                }
 */

/*
*****Show how many reentries
                String[] reentryList = lists.split(",");
                for(int i = 0; i < songList.size(); i++) {
                int total = 0;
                for(int j = 0; j < reentryList.length - 1; j++) {
                    Integer p1 = songList.get(i).getPositionMap().get(reentryList[j]);
                    Integer p2 = songList.get(i).getPositionMap().get(reentryList[j+1]);
                    if(!(p1 == null) && (p2 == null || p2 == 0)) {
                        total++;
                    }
                }
                if(songList.get(i).getPositionMap().get(reentryList[reentryList.length - 1]) != null) {
                    total++;
                }
                System.out.println(songList.get(i).showSong() + ": " + total);
                }
 */

/*
*****Calculate ranking based on four most popular lists
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
                for(int i = 0; i < songList.size(); i++) {
                    Record currentRecord = songList.get(i);
                    double totaal2 = 0; double totaalV = 0; double totaalG = 0; double totaalJ = 0;
                    for(Map.Entry<String, Integer> entry : currentRecord.getPositionMap().entrySet()) {
                        if(Arrays.stream(toplijsten).anyMatch(entry.getKey()::equals)) {
                            if(entry.getKey().startsWith("R2NLA")) {
                                if(entry.getValue() != null) { totaal2 = totaal2 + (2001 - entry.getValue()); }
                            }
                            if(entry.getKey().startsWith("VA")) {
                                if(entry.getValue() != null) { totaalV = totaalV + (1001 - entry.getValue()); }
                            }
                            if(entry.getKey().startsWith("R10A")) {
                                if(entry.getValue() != null) { totaalG = totaalG + (4001 - entry.getValue()); }
                            }
                            if(entry.getKey().startsWith("JA")) {
                                if(entry.getValue() != null) { totaalJ = totaalJ + (2001 - entry.getValue()); }
                            }
                        }
                    }
                    double som = (totaal2 / count2) + (totaalV / countV) + (totaalG / (2 * countG)) + (totaalJ / countJ);
                    songAndPoints.put(currentRecord.showSong(), som);
                }
                songAndPoints = sortHashMapByValuesDouble(songAndPoints);
                for(Map.Entry<String, Double> song : songAndPoints.entrySet()) {
                    System.out.println(song.getKey() + ": " + song.getValue());
                }
 */