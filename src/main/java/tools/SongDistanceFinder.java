package tools;

import dto.Song;
import dto.SongList;
import org.apache.commons.text.similarity.LevenshteinDistance;
import parser.XMLParser;

import java.io.*;
import java.util.*;

import static java.lang.Math.max;

public class SongDistanceFinder {

    public static final XMLParser xmlParser = new XMLParser();

    public static void main(final String[] args) {
        System.out.println("Currently merging allelijstenextended");
        final SongList songList = xmlParser.parseXML("allelijstenextended.xml", new SongList());
        try {
            final FileWriter writer = new FileWriter("allelijstencompareoutput.txt");
            final File comparisonFile = new File("allelijstencompare.txt");
            final BufferedReader reader = new BufferedReader(new FileReader(comparisonFile));
            String line;
            while ((line = reader.readLine()) != null) {
                final String cleanedLine = line.replaceAll("<record><Artiest>", "").replaceAll("</Artiest><Nummer>", "|").replaceAll("</Nummer></record>", "");
                writer.write(calculateShortestDistances(songList, cleanedLine));
                writer.write("\n");
            }
            writer.close();
        } catch (final IOException e) {
            System.out.println("Error while reading file, with exception: " + e);
        }
    }

    private static String calculateShortestDistances(final SongList songList, final String input) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("10 closest matches for song ").append(input).append(":").append("\n");
        final List<SongDistance> distanceList = new ArrayList<>();
        for (final Song song : songList) {
            final String songStringToCheck = song.getArtist() + "|" + song.getTitle();
            final Integer distance = LevenshteinDistance.getDefaultInstance().apply(input, songStringToCheck);
            final double normalizedDistance = (double) distance / (max(input.length(), songStringToCheck.length()));
            distanceList.add(new SongDistance(songStringToCheck, normalizedDistance));
        }
        final SongDistance[] objects = distanceList.toArray(new SongDistance[0]);
        Arrays.sort(objects, Comparator.comparing(song -> song.distance));
        int counter = 0;
        while (counter < 10) {
            final String songName = objects[counter].song;
            final double distance = objects[counter].distance;
            stringBuilder.append("Song: ").append(songName).append(", Distance: ").append(distance == 0 ? "MATCH" : distance).append("\n");
            counter++;
        }
        return stringBuilder.toString();
    }

    private record SongDistance(String song, double distance) {

    }
}
