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
    private Socket socket;
    private PrintWriter output;

    public ClientRunnable(Socket s) throws IOException {
        this.input = new BufferedReader(new InputStreamReader(s.getInputStream()));
        output = new PrintWriter(s.getOutputStream(),true);
        this.socket = s;
    }

    private void printReadyUsers(ResultSet resultSet) throws SQLException {
        while(resultSet.next()) {
            String username = resultSet.getString("username");
            String status = resultSet.getString("status");
            boolean ready = resultSet.getBoolean("ready");
            if (!username.equals(userName) && ready && status.equals("online")){
                System.out.println("Username: " + username + ", Status: " + status + " Ready: " + ready);
            }
        }
    }

    @Override
    public void run() {
        try {
            while(true) {
                Scanner scanner = new Scanner(System.in);
                DBConnector dbHandler = new DBConnector();

                String authenticationError = "Password incorrect. Authentication failed, please try again later.";
                String helpTextTrigger = "Welcome to the server! You can leave server by sending 'exit'.";
                String helpText = "1. Use Private Chat\n2. Use Group Chat\n3. Use Channels\n4. Wait for chat invitations";
                String exitText = "[Left Server]";

                String response = input.readLine();

                if (response.equals(authenticationError)) {
                    System.out.println("[ERROR] " + authenticationError);
                    System.exit(0);
                }
                else if (response.equals(helpTextTrigger)){
                    userName = dbHandler.getUserName(socket.getPort());
                    System.out.println(helpText);
                    System.out.print("Enter operation number: ");
                    String userChoice = scanner.nextLine();
                    if (userChoice.equals("1")){
                        ResultSet resultSet = dbHandler.fetchRecords("Users");
                        printReadyUsers(resultSet);
                        System.out.print("Enter username to start a PvP chat: ");
                        String targetUsername = scanner.nextLine();
                        String chatName = userName + "&" + targetUsername;
                        String chatAttendances = userName + "|" + targetUsername;
                        String message = "Private," + chatName + "," + userName + "," + chatAttendances;
                        output.println(message);
                    }
                    else if (userChoice.equals("2")) {
                        ResultSet resultSet = dbHandler.fetchRecords("Users");
                        printReadyUsers(resultSet);
                        System.out.print("Enter usernames (separated with '|', e.g. Username1|Username2|Username3) to start a group chat: ");
                        String chatAttendances = scanner.nextLine();
                        System.out.println("enter your group's name:");
                        String chatName = scanner.nextLine();
                        String message = "Group," + chatName + "," + userName + "," + chatAttendances;
                        output.println(message);
                    }
                    else if (userChoice.equals("3")){
                        ResultSet resultSet = dbHandler.fetchRecords("Users");
                        printReadyUsers(resultSet);
                        System.out.print("Enter usernames (separated with '|', e.g. Username1|Username2|Username3) to start a channel: ");
                        String chatAttendances = scanner.nextLine();
                        System.out.println("enter your channel's name:");
                        String chatName = scanner.nextLine();
                        String message = "Channel," + chatName + "," + userName + "," + chatAttendances;
                        output.println(message);
                    }
                    else if (userChoice.equals("4")){
                        System.out.println("Waiting for invitations...");
                        dbHandler.alterUserReady(userName, true);
                    }
                    else {
                        System.out.println("[ERROR] Operation number not found.");
                    }
                }
                else if (response.equals(exitText)){
                    System.out.println("Disconnected from the server.\nBye " + userName);
                    output.close();
                    socket.close();
                    System.exit(0);
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