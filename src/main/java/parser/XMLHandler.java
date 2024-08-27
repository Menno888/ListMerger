package parser;

import dto.Song;
import dto.SongList;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import static tools.FieldUtil.*;

public class XMLHandler extends DefaultHandler {

    private final StringBuilder xml = new StringBuilder();
    private boolean building = false;
    private final SongList songList = new SongList();
    private String elementTag;

    public SongList getSongList() {
        return songList;
    }

    Song songToParse;

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) {
        elementTag = qName;

        if (COLUMN_HEADER_RECORD.equals(qName)) {
            building = true;
            songToParse = new Song();
        }
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) {
        if (COLUMN_HEADER_RECORD.equals(qName)) {
            building = false;
            songList.add(songToParse);
        }

        xml.setLength(0);
    }

    @Override
    public void characters(final char[] ch, final int start, final int length) {
        if (building) {
            String value = new String(ch, start, length);
            if (new String(ch, start, length).trim().length() == 0) {
                return;
            }

            if (COLUMN_HEADER_ARTIST.equals(elementTag) || COLUMN_HEADER_SONG.equals(elementTag)) {
                final String xmlResult = xml.append(value).toString();
                value = xmlResult.replace(SEPARATOR_CHARACTER_AMPERSAND, SEPARATOR_CHARACTER_AMPERSAND_XML_SAFE).trim();
                if (COLUMN_HEADER_ARTIST.equals(elementTag)) {
                    songToParse.setArtist(value);
                }
                if (COLUMN_HEADER_SONG.equals(elementTag)) {
                    songToParse.setTitle(value);
                }
            }
            else if (elementTag.startsWith(INFO_COLUMN_MARKER)) {
                songToParse.addInfoToMap(elementTag, value);
            }
            else {
                songToParse.addPositionToMap(elementTag, Integer.parseInt(value));
            }
        }
    }
}
