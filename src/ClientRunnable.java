import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ClientRunnable implements Runnable {

    private final BufferedReader input;
    private String userName;
    private boolean isUserBusy;
    private Socket socket;
    private PrintWriter output;

    public ClientRunnable(Socket s) throws IOException {
        this.input = new BufferedReader(new InputStreamReader(s.getInputStream()));
        output = new PrintWriter(s.getOutputStream(),true);
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
                String helpText = "1. Use Private Chat\n2. Use Group Chat\n3. Use Channels\n4. Wait for chat invitations";

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
                                boolean targetUserBusy = dbHandler.getUserBusy(targetUsername);
                                if (targetUserStatus.equals("online")){
                                    if (!targetUserBusy) {
                                        isUserBusy = true;
                                        String chatName = userName + "&" + targetUsername;
                                        String chatAttendances = userName + "|" + targetUsername;
                                        String message = "Private," + chatName + "," + userName + "," + chatAttendances + ",true";
                                        output.println(message);
                                    } else {
                                        System.out.println("User " + targetUsername + " is busy now!");
                                    }
                                } else {
                                    System.out.println("User " + targetUsername + " is currently offline.");
                                }
                            } else {
                                System.out.println("User doesn't exists.");
                            }
                        }
                        else if (userChoice.equals("4")){
                            System.out.println("Waiting for invitations...");
                            isUserBusy = true;
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