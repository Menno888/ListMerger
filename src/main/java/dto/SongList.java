package dto;

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
                final boolean fileCreated = file.createNewFile();
                System.out.println("Created file with success status: " + fileCreated);
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
            assert writer != null;
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
            writer.newLine();
            writer.write("<top2000database2014>");
            writer.newLine();
            for (final Song song : this) {
                final String output = convertSongToXML(song, positions, prettyPrint, countPositions);
                writer.write(output);
                writer.newLine();
            }
            writer.write("</top2000database2014>");
            writer.close();
        } catch (final IOException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            try {
                assert writer != null;
                writer.close();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Output to " + outFile);
    }

    public boolean contains(final Song song) {
        return this.stream().anyMatch(r -> (r.getArtist()).equals(song.getArtist()) && (r.getTitle()).equals(song.getTitle()));
    }

    private String convertSongToXML(final Song song, final String positions, final String prettyPrint, final String countPositions) {
        final StringBuilder stringBuilder = new StringBuilder();
        final String indentation = "y".equals(prettyPrint) ? "    " : "";
        final String lineBreak = "y".equals(prettyPrint) ? "\n" : "";

        stringBuilder.append(indentation).append("<record>").append(lineBreak);
        stringBuilder.append(indentation.repeat(2)).append("<Artiest>").append(song.getArtist()).append("</Artiest>").append(lineBreak);
        stringBuilder.append(indentation.repeat(2)).append("<Nummer>").append(song.getTitle()).append("</Nummer>").append(lineBreak);
        if ("y".equals(countPositions)) {
            final long numberOfPositions = getPositionsCount(song, positions);
            stringBuilder.append(indentation.repeat(2)).append("<appearances>").append(numberOfPositions).append("</appearances>").append(lineBreak);
        }
        if (!"n".equals(positions)) {
            for (final Map.Entry<String, Integer> pair : song.getPositionMap().entrySet()) {
                if ("y".equals(positions) || pair.getKey().substring(pair.getKey().length() - 4).equals(positions)) {
                    stringBuilder.append(indentation.repeat(2)).append("<").append(pair.getKey()).append(">").append(pair.getValue()).append("</").append(pair.getKey()).append(">").append(lineBreak);
                }
            }
        }
        for (final Map.Entry<String, Object> pair : song.getAdditionalInformationMap().entrySet()) {
            stringBuilder.append(indentation.repeat(2)).append("<").append(pair.getKey()).append(">").append(pair.getValue()).append("</").append(pair.getKey()).append(">").append(lineBreak);
        }
        stringBuilder.append(indentation).append("</record>");

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
