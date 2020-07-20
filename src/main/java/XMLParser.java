import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;

class XMLParser {

    private final StringBuilder xml = new StringBuilder();
    private boolean building = false;
    private SongList songList = new SongList();
    private String elementTag;

    XMLParser()
    {
        SongExceptions.loadExceptions();
    }

    SongList parseXML(String file, SongList list) {

        songList = list;

        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

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
                            record.addPositionToMap(elementTag, Integer.parseInt(value));
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

    private void addToArrayList(Record record) {
        boolean merged = false;
        for (Record song : songList) {
            if (record.getArtiest().equals(song.getArtiest()) && record.getNummer().equals(song.getNummer())) {
                LinkedHashMap<String, Integer> temp = song.getPositionMap();
                temp.putAll(record.getPositionMap());
                song.setPositionMap(temp);
                System.out.println("Merged: " + record.showSong());
                merged = true;
            }
        }
        if(!merged) {
            songList.add(record);
            System.out.println("Copied over: " + record.showSong());
        }
    }
}
