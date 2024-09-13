package parser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import dto.Song;
import dto.SongList;
import org.xml.sax.SAXException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public class XMLParser {

    private SongList songList = new SongList();

    public SongList parseXML(String file, final SongList list) {

        songList = list;

        final SAXParserFactory factory = SAXParserFactory.newDefaultInstance();
        try {
            final SAXParser saxParser = factory.newSAXParser();
            final XMLHandler handler = new XMLHandler();

            if (!file.endsWith(".xml")) {
                file = file + ".xml";
            }
            saxParser.parse(file, handler);

            final SongList listFromParse = handler.getSongList();
            for (final Song songFromList : listFromParse) {
                addToArrayList(songFromList);
            }
            System.out.println("Succesfully merged/initialized " + file);
        } catch (final FileNotFoundException e) {
            System.out.println("File not found, try again");
        } catch (final ParserConfigurationException | IOException | SAXException e) {
            System.out.println("Couldn't parse file, exception: " + e);
        }

        return list;
    }

    private void addToArrayList(final Song songToAdd) {
        boolean merged = false;
        for (final Song song : songList) {
            if (songToAdd.getArtist().equals(song.getArtist()) && songToAdd.getTitle().equals(song.getTitle())) {
                for (final Map.Entry<String, Integer> entry : songToAdd.getPositionMap().entrySet()) {
                    song.addPositionToMap(entry.getKey(), entry.getValue());
                }
                for (final Map.Entry<String, Object> entry : songToAdd.getAdditionalInformationMap().entrySet()) {
                    song.addInfoToMap(entry.getKey(), entry.getValue());
                }
                merged = true;
            }
        }
        if (!merged) {
            songList.add(songToAdd);
        }
    }
}
