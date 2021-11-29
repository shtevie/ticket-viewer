import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

/** interacts with the API */
public class APIUtil {

    /**
     * Connects to API and fetches all of the tickets based on the details provided
     *
     * @param subdomain for API
     * @param email to access API
     * @param token to access API
     * @return tickets returned from API
     * @throws IOException
     */
    public HashMap<Long, JSONObject> getTickets(String subdomain, String email, String token) throws IOException, ParseException {
            /** for api auth */
            String cred = email + "/token:" + token;
            String encoded = "Basic " + new String(Base64.getEncoder().encode(cred.getBytes(StandardCharsets.UTF_8)));

            /** holds all tickets */
            HashMap<Long, JSONObject> map = new HashMap<>();

            URL url = new URL("https://" + subdomain + "/api/v2/tickets?page[size]=100");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            configConnection(connection, encoded);
            JSONObject jsonObject = pushToMap(map, connection);
            // continue pagination
            boolean more = true;
            while(more) {
                    JSONObject obj = (JSONObject) jsonObject.get("meta");
                    if((boolean) obj.get("has_more")) {
                            more = true;
                            obj = (JSONObject) jsonObject.get("links");
                            url = new URL((String) obj.get("next"));
                            connection = (HttpURLConnection) url.openConnection();
                            configConnection(connection, encoded);
                            jsonObject = pushToMap(map, connection);
                    }
                    else {
                            more = false;
                    }
            }
            return map;
    }

    /** pushes the response from a request into a map */
    private JSONObject pushToMap(HashMap<Long, JSONObject> map, HttpURLConnection connection) throws IOException, ParseException {
            InputStream in = connection.getInputStream();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(in, "UTF-8"));

            // will return a JSONArray so cast is safe
            JSONArray arr = (JSONArray) jsonObject.get("tickets");
            JSONObject curr;
            for(int i=0; i<arr.size(); i++) {
                    curr = (JSONObject) arr.get(i);
                    long id = (long) curr.get("id");
                    map.put(id, curr);
            }
            return jsonObject;
    }

    /** configures a connection for accessing api */
    private void configConnection(HttpURLConnection connection, String encoded) throws ProtocolException {
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", encoded);
    }
}
