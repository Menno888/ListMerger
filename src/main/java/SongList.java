import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class SongList extends ArrayList<Record> {

    public ArrayList<String> tagCheckup() {

        ArrayList<String> tagList = new ArrayList<>();
        for (Record currentRecord : this) {
            for (Map.Entry<String, Integer> entry : currentRecord.getPositionMap().entrySet()) {
                if (!tagList.contains(entry.getKey())) {
                    tagList.add(entry.getKey());
                }
            }
        }
        return tagList;
    }

    public void normalize() {
        Iterator<Record> it = this.iterator();
        while (it.hasNext()) {
            Record item = it.next();
            if (item.getPositionMap().size() == 0) {
                it.remove();
                System.out.println("Removed " + item.showSong());
            }
        }
        System.out.println("There's " + this.size() + " records left");
    }
}
