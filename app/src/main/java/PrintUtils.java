import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import java.io.IOException;
import java.util.Map;

public class PrintUtils {

    /** represents all tickets */
    private Map<Long, JSONObject> map;

    /** number of entities per page */
    private static final int PAGE_LIMIT = 25;

    /**
     * Creates a PrintUtils object
     *
     * @param map representation of all tickets
     */
    public PrintUtils(Map<Long, JSONObject> map) {
        this.map = map;
    }

    /**
     * Creates page p
     * @param p
     * @return page p
     * @throws IllegalArgumentException if the page number requested is out of bounds
     */
    public String printPage(int p) throws NoSuchFieldException {
        StringBuilder s = new StringBuilder();
        // if the page is out of bounds
        if((p-1) * PAGE_LIMIT > map.size() || p < 1) throw new NoSuchFieldException();
        s.append("------------------------------------------------------------------------------------\n");
        s.append(String.format("%10s %50s %10s %10s\n", "ID", "SUBJECT", "STATUS", "TYPE"));
        s.append("------------------------------------------------------------------------------------\n");

        int counter = 1;
        if (map.size() <= PAGE_LIMIT * (p - 1)) {
            for (long id : map.keySet()) {
                JSONObject obj = map.get(id);
                s.append(String.format("%10d %50s %10s %10s\n", id, obj.get("subject"), obj.get("status"), obj.get("type")));
            }
        }
        else {
            for (long id : map.keySet()) {
                JSONObject obj = map.get(id);
                if (counter > PAGE_LIMIT * (p - 1) && counter <= PAGE_LIMIT * (p)) {
                    s.append(String.format("%10d %50s %10s %10s\n", id, obj.get("subject"), obj.get("status"), obj.get("type")));
                }
                counter++;
            }

            s.append("------------------------------------------------------------------------------------\n\n");
        }

        return s.toString();
    }

    /**
     * Prints details of a single ticket
     * @param id id of the ticket
     * @throws IllegalArgumentException if the id does not exist
     * @throws IOException
     * @return the details for the ticket
     */
    public String printTicket(long id) throws NoSuchFieldException, IOException {
        if(!map.containsKey(id)) throw new NoSuchFieldException();
        JSONObject obj = map.get(id);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        return json;
    }
}
