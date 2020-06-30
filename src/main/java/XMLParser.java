import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

class XMLParser {

    private final StringBuilder xml = new StringBuilder();
    private boolean building = false;
    private ArrayList<Record> songList = new ArrayList<>();
    private int songValue;
    private String elementTag;

    XMLParser()
    {
        SongExceptions.loadExceptions();
    }

    ArrayList<Record> parseXML(String file, ArrayList<Record> list) {

        songList = list;

        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            //Build custom JSON
            DefaultHandler handler = new DefaultHandler() {

                private Record record = new Record();

                public void startElement(String uri, String localName,
                                         String qName, Attributes attributes) {

                    elementTag = qName;

                    if ("record".equals(qName)) {

                        building = true;
                    }
                }

                public void endElement(String uri, String localName,
                                       String qName) {

                    if ("record".equals(qName)) {
                        building = false;
                        SongExceptions.songExceptionConverter(record);
                        addToArrayList(record);
                        record = new Record();
                    }

                    xml.setLength(0);

                }

                public void characters(char[] ch, int start, int length) {
                    if (building) {
                        String value = new String(ch, start, length).trim();
                        if (value.length() == 0) return; // ignore white space

                        if("Artiest".equals(elementTag) || "Nummer".equals(elementTag)) {
                            if("Artiest".equals(elementTag)) {
                                RecordCleaner.addCData(xml, value);
                                String xmlResult = xml.toString();
                                value = RecordCleaner.removeCData(xmlResult);
                                value = RecordCleaner.resolveAmpersands(value);
                                record.setArtiest(value);
                            }
                            if("Nummer".equals(elementTag)) {
                                RecordCleaner.addCData(xml, value);
                                String xmlResult = xml.toString();
                                value = RecordCleaner.removeCData(xmlResult);
                                value = RecordCleaner.resolveAmpersands(value);
                                record.setNummer(value);
                            }
                        }
                        else {
                            songValue = Integer.parseInt(value);
                            record.addPositionToMap(elementTag, songValue);
                        }
                    }
                }

            };

            if (!file.endsWith(".xml")) {
                file = file + ".xml";
            }
            saxParser.parse(file, handler);

            System.out.println("Succesfully merged/initialized " + file);

        } catch (FileNotFoundException e) {
            System.out.println("File not found, try again");
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

        return songList;

    }

    private void addToArrayList(Record r) {
        boolean merged = false;
        for (Record song : songList) {
            if (r.getArtiest().equals(song.getArtiest()) && r.getNummer().equals(song.getNummer())) {
                LinkedHashMap<String, Integer> temp = song.getPositionMap();
                temp.putAll(r.getPositionMap());
                song.setPositionMap(temp);
                System.out.println("Merged: " + r.showSong());
                merged = true;
            }
        }
        if(!merged) {
            songList.add(r);
            System.out.println("Copied over: " + r.showSong());
        }
    }
}