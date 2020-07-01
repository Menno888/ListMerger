import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class Writer {

    public static void output(SongList songList, String outFile, String positions, String prettyPrint, String countPositions) {
        if (!outFile.endsWith(".xml")) {
            outFile = outFile + ".xml";
        }
        if (".xml".equals(outFile)) {
            outFile = System.currentTimeMillis() + ".xml";
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
            for (Record song : songList) {
                String output = convertRecord(song, positions, prettyPrint, countPositions);
                writer.write(output);
                writer.newLine();
                System.out.println("Wrote: " + song.showSong());
            }
            writer.write("</top2000database2014>");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Output to " + outFile);
    }

    private static String convertRecord(Record record, String positions, String prettyPrint, String countPositions) {
        StringBuilder stringBuilder = new StringBuilder();
        String indentation;
        String lineBreak;

        if("y".equals(prettyPrint)) {indentation = "    "; lineBreak = "\n";} else {indentation = ""; lineBreak = "";}

        stringBuilder.append(indentation + "<record>" + lineBreak);
        stringBuilder.append(repeat(indentation, 2) + "<Artiest>" + record.getArtiest() + "</Artiest>" + lineBreak);
        stringBuilder.append(repeat(indentation, 2) + "<Nummer>" + record.getNummer() + "</Nummer>" + lineBreak);
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

    private static long getPositionsCount(Record record, String positions) {
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

    private static String repeat(String str, int times) {
        StringBuilder sb = new StringBuilder(str.length() * times);
        for(int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}
