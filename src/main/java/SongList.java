import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.lang.System.currentTimeMillis;

public class SongList extends ArrayList<Record> {

    public void outputToFile() {
        final String outFile = "out" + currentTimeMillis();
        outputToFile(outFile, "y,n,n");
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
            outFile = currentTimeMillis() + ".xml";
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
            writer.write("<top2000database2014 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
            writer.newLine();
            for (final Record song : this) {
                final String output = convertRecord(song, positions, prettyPrint, countPositions);
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

    private String convertRecord(final Record record, final String positions, final String prettyPrint, final String countPositions) {
        final StringBuilder stringBuilder = new StringBuilder();
        String indentation;
        String lineBreak;

        if ("y".equals(prettyPrint)) {indentation = "    "; lineBreak = "\n";} else {indentation = ""; lineBreak = "";}

        stringBuilder.append(indentation + "<record>" + lineBreak);
        stringBuilder.append(repeat(indentation, 2) + "<Artiest>" + record.getArtist() + "</Artiest>" + lineBreak);
        stringBuilder.append(repeat(indentation, 2) + "<Nummer>" + record.getTitle() + "</Nummer>" + lineBreak);
        if ("y".equals(countPositions)) {
            final long numberOfPositions = getPositionsCount(record, positions);
            stringBuilder.append(repeat(indentation, 2) + "<appearances>" + numberOfPositions + "</appearances>" + lineBreak);
        }
        if (!"n".equals(positions)) {
            for (final Map.Entry<String, Integer> pair : record.getPositionMap().entrySet()) {
                if ("y".equals(positions) || pair.getKey().substring(pair.getKey().length() - 4).equals(positions)) {
                    stringBuilder.append(repeat(indentation, 2) + "<" + pair.getKey() + ">" + pair.getValue() + "</" + pair.getKey() + ">" + lineBreak);
                }
            }
        }
        for (final Map.Entry<String, Object> pair : record.getAdditionalInformationMap().entrySet()) {
            stringBuilder.append(repeat(indentation, 2) + "<" + pair.getKey() + ">" + pair.getValue() + "</" + pair.getKey() + ">" + lineBreak);
        }
        stringBuilder.append(indentation + "</record>");

        return stringBuilder.toString();
    }

    private long getPositionsCount(final Record record, final String positions) {
        final long numberOfPositions;
        if (!"n".equals(positions) && !"y".equals(positions)) {
            numberOfPositions = record.getPositionMap().entrySet().stream().filter(e -> positions.equals(e.getKey().substring(e.getKey().length() - 4))).count();
        }
        else {
            numberOfPositions = record.getPositionMap().entrySet().size();
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
        for (final Record currentRecord : this) {
            for (final Map.Entry<String, Integer> entry : currentRecord.getPositionMap().entrySet()) {
                if (!tagList.contains(entry.getKey())) {
                    tagList.add(entry.getKey());
                }
            }
        }
        return tagList;
    }

    public void normalize() {
        final Iterator<Record> it = this.iterator();
        while (it.hasNext()) {
            final Record item = it.next();
            if (item.getPositionMap().size() == 0) {
                it.remove();
                System.out.println("Removed " + item.showSong());
            }
        }
        System.out.println("There's " + this.size() + " records left");
    }

    public void outputToScreen() {
        for (final Record currentRecord : this) {
            System.out.println(currentRecord.showSong());
        }
        System.out.println("There's " + this.size() + " records left");
    }
}
