import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PrintUtilsTest {

    String subdomain = "zccstudent3087.zendesk.com";
    String email = "";
    String token = "";

    /** tests printing ticket table */
    @Test
    void testPagination() throws IOException, ParseException, NoSuchFieldException {
        APIUtil apiUtil = new APIUtil();
        Map<Long, JSONObject> map = apiUtil.getTickets(subdomain, email, token);
        PrintUtils printUtils = new PrintUtils(map);

        // ensure all pages have less than 25 entries
        for(int i=1; i<5; i++) {
            String page = printUtils.printPage(i);
            String[] lines = page.split("\r\n|\r|\n");
            assertTrue(lines.length <= 25 + 4); // rows + table formatting
        }

        // ensure we cannot print out of bounds
        assertThrows(NoSuchFieldException.class, () -> printUtils.printPage(6));
        assertThrows(NoSuchFieldException.class, () -> printUtils.printPage(-1));

    }

    /** tests fetching a single ticket */
    @Test
    void testFetchTicket() throws IOException, ParseException, NoSuchFieldException {
        APIUtil apiUtil = new APIUtil();
        Map<Long, JSONObject> map = apiUtil.getTickets(subdomain, email, token);
        PrintUtils printUtils = new PrintUtils(map);

        // test for valid tickets
        for(int i=1; i<101; i++) {
            assertNotNull(printUtils.printTicket((long) i));
        }

        // invalid tickets
        assertThrows(NoSuchFieldException.class, () -> printUtils.printTicket(102));
        assertThrows(NoSuchFieldException.class, () -> printUtils.printTicket(0));

    }
}
