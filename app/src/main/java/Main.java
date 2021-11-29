import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean terminate = false;
        Scanner sc = new Scanner(System.in);;
        boolean validRequest = false;
        while(!validRequest) {
            // user specifies API to connect to
            System.out.println("Enter subdomain (i.e. zccstudent3087.zendesk.com) :");
            String subdomain = sc.nextLine();
            System.out.println("Enter user email:");
            String email = sc.nextLine();
            System.out.println("Enter api token:");
            String token = sc.nextLine();

            String cred = email + "/token:" + token;
            String encoded = "Basic " + new String(Base64.getEncoder().encode(cred.getBytes(StandardCharsets.UTF_8)));

            URL url = null;
            try {
                url = new URL("https://" + subdomain + "/api/v2/tickets");
                System.out.println("Connecting to: " + url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty ("Authorization", encoded);
                String in = connection.getResponseMessage();
                System.out.println(in);
                validRequest = true;
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Invalid URL");
            }

        }

//        while(!terminate) {
//
//        }

        sc.close();
    }
}
