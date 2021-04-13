import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ClientRunnable implements Runnable {

    private final BufferedReader input;
    private String userName;
    private boolean isUserBusy;
    private Socket socket;

    public ClientRunnable(Socket s) throws IOException {
        this.input = new BufferedReader(new InputStreamReader(s.getInputStream()));
        this.socket = s;

    }

    @Override
    public void run() {
        try {
            while(true) {
                Scanner scanner = new Scanner(System.in);
                DBConnector dbHandler = new DBConnector();

                String AuthenticationError = "Password incorrect. Authentication failed, please try again later.";
                String helpTextTrigger = "Welcome to the server! You can leave server by sending 'exit'.";
                String helpText = "1. Use Private Chat\n2. Use Group Chat\n3. Use Channels";

                String response = input.readLine();

                if (response.equals(AuthenticationError)) {
                    System.out.println("[ERROR] " + AuthenticationError);
                    System.exit(0);
                }
                else if (response.equals(helpTextTrigger)){
                    userName = dbHandler.getUserName(socket.getPort());
                    boolean isUserBusy = dbHandler.getUserBusy(userName);

                    while (!isUserBusy){
                        System.out.println(helpText);
                        System.out.print("Enter operation number: ");
                        String userChoice = scanner.nextLine();
                        if (userChoice.equals("1")){
                            ResultSet resultSet = dbHandler.fetchRecords("Users");
                            dbHandler.printRecords(resultSet);
                            System.out.println("Enter username to start a PvP chat:");
                            String targetUsername = scanner.nextLine();
                            if (dbHandler.userExists(targetUsername)){
                                String targetUserStatus = dbHandler.getUserStatus(targetUsername);
                                if (targetUserStatus.equals("online")){
                                    System.out.println("OK! will implement it soon!");
                                    System.out.println("User " + userName + " is busy now!");
                                    isUserBusy = true;
                                    dbHandler.alterUserBusy(userName, isUserBusy);
                                } else {
                                    System.out.println("User " + targetUsername + " is currently offline.");
                                }
                            } else {
                                System.out.println("User doesn't exists.");
                            }
                        }
                    }
                }
                else {
                    System.out.println(response);
                }
            }
        } catch (IOException | SQLException e) {
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