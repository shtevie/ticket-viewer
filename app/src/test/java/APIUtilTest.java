import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

/** tests for api fetching */
public class APIUtilTest {

    String subdomain = "zccstudent3087.zendesk.com";
    String email = "";
    String token = "";

    @Test
    void testGetTicketsSize() throws IOException, ParseException {
        APIUtil apiUtil = new APIUtil();
        Map<Long, JSONObject> map = apiUtil.getTickets(subdomain, email, token);
        assertEquals(map.size(), 101);
    }

    @Test
    void testGetTicketsException() {
        APIUtil apiUtil = new APIUtil();

        // invalid subdomain
        assertThrows(IOException.class, () -> apiUtil.getTickets("student3087.zendesk.com", email, token));

        // invalid email
        assertThrows(IOException.class, () -> apiUtil.getTickets(subdomain, "", token));

        // invalid token
        assertThrows(IOException.class, () -> apiUtil.getTickets(subdomain, email, ""));

    }
}
