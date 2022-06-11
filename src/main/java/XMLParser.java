import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

class XMLParser {

    private final StringBuilder xml = new StringBuilder();
    private boolean building = false;
    private SongList songList = new SongList();
    private String elementTag;

    SongList parseXML(String file, SongList list) {

        songList = list;

        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {

                private Record record;

                public void startElement(String uri, String localName,
                                         String qName, Attributes attributes) {

                    elementTag = qName;

                    if ("record".equals(qName)) {

                        building = true;
                        record = new Record();
                    }
                }

                public void endElement(String uri, String localName,
                                       String qName) {

                    if ("record".equals(qName)) {
                        building = false;
                        addToArrayList(record);
                    }

                    xml.setLength(0);

                }

                public void characters(char[] ch, int start, int length) {
                    if (building) {
                        String value = new String(ch, start, length);
                        if (new String(ch, start, length).trim().length() == 0) return; // ignore white space

                        if("Artiest".equals(elementTag) || "Nummer".equals(elementTag)) {
                            String xmlResult = xml.append(value).toString();
                            value = xmlResult.replace("&", "&amp;").trim();
                            if("Artiest".equals(elementTag)) {
                                record.setArtist(value);
                            }
                            if("Nummer".equals(elementTag)) {
                                record.setTitle(value);
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
            if (record.getArtist().equals(song.getArtist()) && record.getTitle().equals(song.getTitle())) {
                for (Map.Entry<String, Integer> entry : record.getPositionMap().entrySet()) {
                    song.addPositionToMap(entry.getKey(), entry.getValue());
                }
                merged = true;
            }
        }
        if(!merged) {
            songList.add(record);
        }
    }
}
