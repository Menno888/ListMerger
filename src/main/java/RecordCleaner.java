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

    public static String cleanName(String data) {
        if(data.contains("&amp;")) {
            data = data.replace("( &amp", "(&amp");
            data = data.replace("On The L &amp; N", "On The L&amp;N");
            data = data.replace("Up &amp; Up", "Up&amp;Up");
            if("M &amp; F".equals(data)) {
                data = data.replace("M &amp; F", "M&amp;F");
            }
            data = data.replace("R &amp; J Stone", "R&amp;J Stone");
            if("S &amp; M".equals(data)) {
                data = data.replace("S &amp; M", "S&amp;M");
            }
            data = data.replace("Product G &amp; B", "Product G&amp;B");
            if("Y &amp; T".equals(data)) {
                data = data.replace("Y &amp; T", "Y&amp;T");
            }
            if("W &amp; W".equals(data)) {
                data = data.replace("W &amp; W", "W&amp;W");
            }
        }
        return data;
    }
}
