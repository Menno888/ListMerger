import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Writer {

    public static void output(ArrayList<Record> songList, String outFile, String positions, String pp, String countPositions) {
        File file = new File(outFile);

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileOutputStream fw;
        BufferedWriter bw = null;
        try {
            fw = new FileOutputStream(file.getAbsoluteFile());
            bw = new BufferedWriter(new OutputStreamWriter(fw, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
            bw.newLine();
            bw.write("<top2000database2014 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
            bw.newLine();
            for (Record song : songList) {
                String output = convertRecord(song, positions, pp, countPositions);
                bw.write(output);
                bw.newLine();
                System.out.println("Wrote: " + song.showSong());
            }
            bw.write("</top2000database2014>");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Output to " + outFile);
    }

    private static String convertRecord(Record r, String positions, String pp, String countPositions) {
        StringBuilder testString = new StringBuilder();
        String indentation;
        String lineBreak;

        if("y".equals(pp)) {indentation = "    "; lineBreak = "\n";} else {indentation = ""; lineBreak = "";}

        testString.append(indentation + "<record>" + lineBreak);
        testString.append(repeat(indentation, 2) + "<Artiest>" + r.getArtiest() + "</Artiest>" + lineBreak);
        testString.append(repeat(indentation, 2) + "<Nummer>" + r.getNummer() + "</Nummer>" + lineBreak);
        if("y".equals(countPositions)) {
            long numberOfPositions = getPositionsCount(r, positions);
            testString.append(repeat(indentation, 2) + "<appearances>" + numberOfPositions + "</appearances>" + lineBreak);
        }
        if(!"n".equals(positions)) {
            for(Map.Entry<String, Integer> pair : r.getPositionMap().entrySet()) {
                if("y".equals(positions) || pair.getKey().substring(pair.getKey().length() - 4).equals(positions)) {
                    testString.append(repeat(indentation, 2) + "<" + pair.getKey() + ">" + pair.getValue() + "</" + pair.getKey() + ">" + lineBreak);
                }
            }
        }
        testString.append(indentation + "</record>");

        return testString.toString();
    }

    private static long getPositionsCount(Record r, String positions) {
        LinkedHashMap<String, Integer> copySongList = r.getPositionMap();
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
