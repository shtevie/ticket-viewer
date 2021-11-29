import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

/** main application loop for user to view tickets */
public class Main {

    public static void main(String[] args) {
        boolean terminate = false;
        Scanner sc = new Scanner(System.in);
        APIUtil apiUtil = new APIUtil();

        // prompt user for connection details
        System.out.println("Enter subdomain (i.e. zccstudent3087.zendesk.com) :");
        String subdomain = sc.nextLine();
        System.out.println("Enter user email:");
        String email = sc.nextLine();
        System.out.println("Enter api token:");
        String token = sc.nextLine();

        try {
            // populate tickets
            Map<Long, JSONObject> map = apiUtil.getTickets(subdomain, email, token);
            PrintUtils printUtils = new PrintUtils(map);
            System.out.println(printUtils.printPage(1));

            // main app loop
            while(!terminate) {
                System.out.println("Enter p to change page, t to view ticket, or q to quit.");
                String input = sc.nextLine();
                if(input.equals("q")) terminate = true;
                else if(input.equals("p")) {
                    System.out.println("Enter page number:");
                    input = sc.nextLine();
                    System.out.println(printUtils.printPage(Integer.parseInt(input)));
                }
                else if(input.equals("t")){
                    System.out.println("Enter ticket id:");
                    input = sc.nextLine();
                    long ticketID = Long.parseLong(input);
                    System.out.println(printUtils.printTicket(ticketID));
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to connect to API or API unavailable");
        } catch (ParseException e) {
            System.out.println("Response invalid");
        } catch (NumberFormatException | NoSuchFieldException e) {
            System.out.println("Invalid input.");
        }

        sc.close();
    }
}
