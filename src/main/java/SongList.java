import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.lang.System.currentTimeMillis;

public class SongList extends ArrayList<Song> {

    public void outputToFile() {
        outputToFile("", "y,n,n");
    }

    public void outputToFile(final String outFile) {
        outputToFile(outFile, "y,n,n");
    }

    public void outputToFile(String outFile, final String options) {
        final String[] optionsArray = options.split(",");

        String positions = "y";
        String prettyPrint = "n";
        String countPositions = "n";

        if (!"".equals(optionsArray[0])) {
            positions = optionsArray[0];
            prettyPrint = optionsArray[1];
            countPositions = optionsArray[2];
        }

        if (!outFile.endsWith(".xml")) {
            outFile = outFile + ".xml";
        }
        if (".xml".equals(outFile)) {
            outFile = "out" + currentTimeMillis() + ".xml";
        }
        final File file = new File(outFile);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        FileOutputStream fileStream;
        BufferedWriter writer = null;
        try {
            fileStream = new FileOutputStream(file.getAbsoluteFile());
            writer = new BufferedWriter(new OutputStreamWriter(fileStream, StandardCharsets.UTF_8));
        } catch (final IOException e) {
            e.printStackTrace();
        }

        try {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
            writer.newLine();
            writer.write("<top2000database2014>");
            writer.newLine();
            for (final Song song : this) {
                final String output = convertSong(song, positions, prettyPrint, countPositions);
                writer.write(output);
                writer.newLine();
            }
            writer.write("</top2000database2014>");
            writer.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        System.out.println("Output to " + outFile);
    }

    public boolean contains(final Song song) {
        return this.stream().anyMatch(r -> (r.getArtist()).equals(song.getArtist()) && (r.getTitle()).equals(song.getTitle()));
    }

    private String convertSong(final Song song, final String positions, final String prettyPrint, final String countPositions) {
        final StringBuilder stringBuilder = new StringBuilder();
        String indentation;
        String lineBreak;

        if ("y".equals(prettyPrint)) {indentation = "    "; lineBreak = "\n";} else {indentation = ""; lineBreak = "";}

        stringBuilder.append(indentation + "<record>" + lineBreak);
        stringBuilder.append(repeat(indentation, 2) + "<Artiest>" + song.getArtist() + "</Artiest>" + lineBreak);
        stringBuilder.append(repeat(indentation, 2) + "<Nummer>" + song.getTitle() + "</Nummer>" + lineBreak);
        if ("y".equals(countPositions)) {
            final long numberOfPositions = getPositionsCount(song, positions);
            stringBuilder.append(repeat(indentation, 2) + "<appearances>" + numberOfPositions + "</appearances>" + lineBreak);
        }
        if (!"n".equals(positions)) {
            for (final Map.Entry<String, Integer> pair : song.getPositionMap().entrySet()) {
                if ("y".equals(positions) || pair.getKey().substring(pair.getKey().length() - 4).equals(positions)) {
                    stringBuilder.append(repeat(indentation, 2) + "<" + pair.getKey() + ">" + pair.getValue() + "</" + pair.getKey() + ">" + lineBreak);
                }
            }
        }
        for (final Map.Entry<String, Object> pair : song.getAdditionalInformationMap().entrySet()) {
            stringBuilder.append(repeat(indentation, 2) + "<" + pair.getKey() + ">" + pair.getValue() + "</" + pair.getKey() + ">" + lineBreak);
        }
        stringBuilder.append(indentation + "</record>");

        return stringBuilder.toString();
    }

    private long getPositionsCount(final Song song, final String positions) {
        final long numberOfPositions;
        if (!"n".equals(positions) && !"y".equals(positions)) {
            numberOfPositions = song.getPositionMap().entrySet().stream().filter(e -> positions.equals(e.getKey().substring(e.getKey().length() - 4))).count();
        }
        else {
            numberOfPositions = song.getPositionMap().entrySet().size();
        }

        return numberOfPositions;
    }

    private String repeat(final String str, final int times) {
        final StringBuilder sb = new StringBuilder(str.length() * times);
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    public List<String> tagCheckup() {

        final ArrayList<String> tagList = new ArrayList<>();
        for (final Song currentSong : this) {
            for (final Map.Entry<String, Integer> entry : currentSong.getPositionMap().entrySet()) {
                if (!tagList.contains(entry.getKey())) {
                    tagList.add(entry.getKey());
                }
            }
        }
        return tagList;
    }

    public void normalize() {
        final Iterator<Song> it = this.iterator();
        while (it.hasNext()) {
            final Song item = it.next();
            if (item.getPositionMap().size() == 0) {
                it.remove();
                System.out.println("Removed " + item.showSong());
            }
        }
        System.out.println("There's " + this.size() + " songs left");
    }

    public void outputToScreen() {
        for (final Song currentSong : this) {
            System.out.println(currentSong.showSong());
        }
        System.out.println("There's " + this.size() + " songs left");
    }
}
