package tools;

public final class FieldUtil {

    private FieldUtil() {
        //No-args
    }

    //Filter
    public static final String FILTER_OPTION_NO = "n";
    public static final String FILTER_OPTION_YES = "y";

    //Song
    public static final String INFO_COLUMN_MARKER = "ADD-";

    //Excel
    public static final int SONG_DATA_START_SHEET = 0;
    public static final int HEADER_ROW_NUM = 0;
    public static final int SONG_DATA_START_ROW_NUM = 1;
    public static final int SONG_DATA_START_COL_NUM = 0;
    public static final int ARTIST_COLUMN_NUM = 0;
    public static final int TITLE_COLUMN_NUM = 1;

    //XML
    public static final String COLUMN_HEADER_RECORD = "record";
    public static final String COLUMN_HEADER_ARTIST = "Artiest";
    public static final String COLUMN_HEADER_SONG = "Nummer";

    //Escape characters
    public static final String SEPARATOR_CHARACTER_COMMA = ",";
    public static final String SEPARATOR_CHARACTER_AMPERSAND = "&";
    public static final String SEPARATOR_CHARACTER_AMPERSAND_XML_SAFE = "&amp;";
    public static final String SEPARATOR_CHARACTER_PIPE = "|";
    public static final String SEPARATOR_CHARACTER_HYPHEN_SPACED = " - ";

    //ToplijstenMergerTools
    public static final String COLUMN_HEADER_MINIMUM = "MIN";
    public static final String COLUMN_HEADER_MAXIMUM = "MAX";
    public static final String COLUMN_HEADER_POSITION = "POS";
    public static final String COLUMN_HEADER_COUNT = "NUM";
    public static final String COLUMN_HEADER_POINTS = "POINTS";
    public static final String COLUMN_HEADER_AVERAGE = "AVERAGE";
    public static final String COLUMN_HEADER_PERCENTAGE = "PERCENTAGE";
    public static final String COLUMN_HEADER_REMAINDER = "REMAINDER";
    public static final String COLUMN_HEADER_NONE = "NONE";
}
