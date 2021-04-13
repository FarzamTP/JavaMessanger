import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;


public class ServerThread extends Thread {
    private Socket socket;
    private ArrayList<ServerThread> threadList;
    private PrintWriter output;
    private String userName;
    private String status;
    private boolean busy;
    private String chatName;

    DBConnector dbHandler = new DBConnector();

    public ServerThread(Socket socket, ArrayList<ServerThread> threads) {
        this.socket = socket;
        this.threadList = threads;
    }

    @Override
    public void run() {

        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(),true);

            while(true) {
                String userInput = input.readLine();
                if(userInput.equalsIgnoreCase("exit")) {
                    String status = "offline";
                    dbHandler.alterUserStatus(userName, status);
                    String message = userName + " left the server!";
                    System.out.println(message);
                    printToALlClients(message);
                    break;
                } else {
                    // e.g. "Username, Password, FirstTimeLoggedIn"
                    if (userInput.split(",").length == 3){
                        int userPort = socket.getPort();
                        String[] tokens = userInput.split(",");
                        userName = tokens[0].split(":")[1];
                        String userPassword = tokens[1].split(":")[1];
                        boolean userFirstTimeLoggedIn = Boolean.parseBoolean(tokens[2].split(":")[1]);

                        if (userFirstTimeLoggedIn){
                            status = "online";
                            busy = false;
                            chatName = "null";
                            dbHandler.insertUser(userPort, userName, userPassword, status, busy, chatName);
                            String message = "User " + userName + " joined us!";
                            System.out.println(message);
                            printToALlClients(message);
                            output.println("Welcome to the server! You can leave server by sending 'exit'.");
                        } else {
                            if (dbHandler.authenticateUser(userName, userPassword)){
                                status = "online";
                                dbHandler.alterUserStatus(userName, status);
                                String message = "User " + userName + " has logged in!";
                                System.out.println(message);
                                printToALlClients(message);
                                output.println("Welcome to the server! You can leave server by sending 'exit'.");
                            } else {
                                System.out.println("User " + userName + " failed to log in.");
                                output.println("Password incorrect. Authentication failed, please try again later.");
                            }
                        }

                    } else if (userInput.split(",").length == 5){
                        String chatType = userInput.split(",")[0];
                        String chatName = userInput.split(",")[1];
                        String chatOwner = userInput.split(",")[2];
                        String chatAttendances = userInput.split(",")[3];
                        boolean chatActive = Boolean.parseBoolean(userInput.split(",")[4]);

                        ArrayList<String> attendancesArrayList = splitAndConvertToArrayList(chatAttendances);

                        Chat privateChat = new Chat(chatType, chatName, chatOwner, chatAttendances, chatActive);
                        privateChat.save();

                        String message = "Welcome to " + chatType + " Chat '" + chatName + "' started by " + chatOwner + " with " + chatAttendances;
                        System.out.println(message);
                        setChatAttendancesChat(attendancesArrayList, chatName);
                        setChatAttendancesBusy(attendancesArrayList);
                        chatAttendancesInformer(attendancesArrayList, message);

                    }
                    else if (userInput.split("\\|")[0].split(":")[0].equals("ChatName")){
                        String chatName = userInput.split("\\|")[0].split(":")[1];
                        String message = "[" + chatName + "] " + userName + ": " + userInput.split("\\|")[1];
                        System.out.println(message);
                        String chatAttendances = dbHandler.getChatAttendances(chatName);
                        ArrayList<String> attendancesArrayList = splitAndConvertToArrayList(chatAttendances);
                        chatAttendancesInformer(attendancesArrayList, message);
                    }
                    else {
                        String message = userName + ": " + userInput;
                        System.out.println(message);
                        printToALlClients(message);
                    }
                }
            }
        } catch (Exception e) {
            try {
                dbHandler.alterUserStatus(userName, "offline");
            } catch (SQLException error) {
                error.printStackTrace();
            }
            System.out.println(userName + " disconnected unexpectedly!");
        }
    }

    private void setChatAttendancesChat(ArrayList<String> attendancesUsernames, String chatName) throws SQLException {
        for (String username : attendancesUsernames){
            dbHandler.alterUserChat(username, chatName);
        }
    }

    private void setChatAttendancesBusy(ArrayList<String> attendancesUsernames) throws SQLException {
        for (String username : attendancesUsernames){
            dbHandler.alterUserBusy(username, true);
        }
    }

    private ArrayList<String> splitAndConvertToArrayList(String attendances){
        ArrayList<String> attendancesArrayList = new ArrayList<String>();
        for (String attendanceUsername : attendances.split("\\|")){
            attendancesArrayList.add(attendanceUsername);
        }
        return attendancesArrayList;
    }

    private void chatAttendancesInformer(ArrayList<String> attendancesUsernames, String message) {
        for(ServerThread st: threadList) {
            if (attendancesUsernames.contains(st.userName)){
                st.output.println(message);
            }
        }
    }

    private void printToALlClients(String outputString) {
        for(ServerThread st: threadList) {
            if (st.socket == socket){
            } else {
                st.output.println(outputString);
            }
        }
    }
}