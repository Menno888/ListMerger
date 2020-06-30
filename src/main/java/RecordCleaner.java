public class RecordCleaner {

    public static void addCData(StringBuilder builder, String value) {
        builder.append("<![CDATA[").append(value).append("]]>");
    }

    public static String removeCData(String data) {
        data = data.replace("]]>", "");
        data = data.replace("<![CDATA[", "");
        return data;
    }

    public static String resolveAmpersands(String data) {
        data = data.replace("&", " &amp; ");
        return data;
    }
}
