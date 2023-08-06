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

    SongList parseXML(String file, final SongList list) {

        songList = list;

        try {

            final SAXParserFactory factory = SAXParserFactory.newInstance();
            final SAXParser saxParser = factory.newSAXParser();

            final DefaultHandler handler = new DefaultHandler() {

                private Record record;

                public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) {
                    elementTag = qName;

                    if ("record".equals(qName)) {
                        building = true;
                        record = new Record();
                    }
                }

                public void endElement(final String uri, final String localName, final String qName) {
                    if ("record".equals(qName)) {
                        building = false;
                        addToArrayList(record);
                    }

                    xml.setLength(0);
                }

                public void characters(final char[] ch, final int start, final int length) {
                    if (building) {
                        String value = new String(ch, start, length);
                        if (new String(ch, start, length).trim().length() == 0) return; // ignore white space

                        if ("Artiest".equals(elementTag) || "Nummer".equals(elementTag)) {
                            final String xmlResult = xml.append(value).toString();
                            value = xmlResult.replace("&", "&amp;").trim();
                            if ("Artiest".equals(elementTag)) {
                                record.setArtist(value);
                            }
                            if ("Nummer".equals(elementTag)) {
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

        } catch (final FileNotFoundException e) {
            System.out.println("File not found, try again");
        } catch (final ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

        return songList;

    }

    private void addToArrayList(final Record record) {
        boolean merged = false;
        for (final Record song : songList) {
            if (record.getArtist().equals(song.getArtist()) && record.getTitle().equals(song.getTitle())) {
                for (final Map.Entry<String, Integer> entry : record.getPositionMap().entrySet()) {
                    song.addPositionToMap(entry.getKey(), entry.getValue());
                }
                for (final Map.Entry<String, Object> entry : record.getAdditionalInformationMap().entrySet()) {
                    song.addInfoToMap(entry.getKey(), entry.getValue());
                }
                merged = true;
            }
        }
        if (!merged) {
            songList.add(record);
        }
    }
}
