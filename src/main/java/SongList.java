import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.System.currentTimeMillis;

public class SongList extends ArrayList<Record> {

    public void outputToFile() {
        String outFile = "out" + currentTimeMillis();
        outputToFile(outFile, "y,n,n");
    }

    public void outputToFile(String outFile) {
        outputToFile(outFile, "y,n,n");
    }

    public void outputToFile(String outFile, String options) {
        String[] optionsArray = options.split(",");

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
        File file = new File(outFile);

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileOutputStream fileStream;
        BufferedWriter writer = null;
        try {
            fileStream = new FileOutputStream(file.getAbsoluteFile());
            writer = new BufferedWriter(new OutputStreamWriter(fileStream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
            writer.newLine();
            writer.write("<top2000database2014 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
            writer.newLine();
            for (Record song : this) {
                String output = convertRecord(song, positions, prettyPrint, countPositions);
                writer.write(output);
                writer.newLine();
            }
            writer.write("</top2000database2014>");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Output to " + outFile);
    }

    private String convertRecord(Record record, String positions, String prettyPrint, String countPositions) {
        StringBuilder stringBuilder = new StringBuilder();
        String indentation;
        String lineBreak;

        if("y".equals(prettyPrint)) {indentation = "    "; lineBreak = "\n";} else {indentation = ""; lineBreak = "";}

        stringBuilder.append(indentation + "<record>" + lineBreak);
        stringBuilder.append(repeat(indentation, 2) + "<Artiest>" + record.getArtist() + "</Artiest>" + lineBreak);
        stringBuilder.append(repeat(indentation, 2) + "<Nummer>" + record.getTitle() + "</Nummer>" + lineBreak);
        if("y".equals(countPositions)) {
            long numberOfPositions = getPositionsCount(record, positions);
            stringBuilder.append(repeat(indentation, 2) + "<appearances>" + numberOfPositions + "</appearances>" + lineBreak);
        }
        if(!"n".equals(positions)) {
            for(Map.Entry<String, Integer> pair : record.getPositionMap().entrySet()) {
                if("y".equals(positions) || pair.getKey().substring(pair.getKey().length() - 4).equals(positions)) {
                    stringBuilder.append(repeat(indentation, 2) + "<" + pair.getKey() + ">" + pair.getValue() + "</" + pair.getKey() + ">" + lineBreak);
                }
            }
        }
        stringBuilder.append(indentation + "</record>");

        return stringBuilder.toString();
    }

    private long getPositionsCount(Record record, String positions) {
        LinkedHashMap<String, Integer> copySongList = record.getPositionMap();
        long numberOfPositions;
        if(!"n".equals(positions) && !"y".equals(positions)) {
            numberOfPositions = copySongList.entrySet().stream().filter(e -> positions.equals(e.getKey().substring(e.getKey().length() - 4))).count();
        }
        else {
            numberOfPositions = copySongList.entrySet().size();
        }

        return numberOfPositions;
    }

    private String repeat(String str, int times) {
        StringBuilder sb = new StringBuilder(str.length() * times);
        for(int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    public ArrayList<String> tagCheckup() {

        ArrayList<String> tagList = new ArrayList<>();
        for (Record currentRecord : this) {
            for (Map.Entry<String, Integer> entry : currentRecord.getPositionMap().entrySet()) {
                if (!tagList.contains(entry.getKey())) {
                    tagList.add(entry.getKey());
                }
            }
        }
        return tagList;
    }

    public void normalize() {
        Iterator<Record> it = this.iterator();
        while (it.hasNext()) {
            Record item = it.next();
            if (item.getPositionMap().size() == 0) {
                it.remove();
                System.out.println("Removed " + item.showSong());
            }
        }
        System.out.println("There's " + this.size() + " records left");
    }

    public void outputToScreen() {
        for(Record currentRecord : this) {
            System.out.println(currentRecord.showSong());
        }
        System.out.println("There's " + this.size() + " records left");
    }
}
