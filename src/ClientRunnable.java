import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientRunnable implements Runnable {

    private final BufferedReader input;

    public ClientRunnable(Socket s) throws IOException {
        this.input = new BufferedReader(new InputStreamReader(s.getInputStream()));
    }

    @Override
    public void run() {
        try {
            while(true) {
                String AuthenticationError = "Password incorrect. Authentication failed, please try again later.";
                String response = input.readLine();
                if (response.equals(AuthenticationError)) {
                    System.out.println(AuthenticationError);
                    System.exit(0);
                }
                System.out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}