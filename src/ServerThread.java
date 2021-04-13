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

    public ServerThread(Socket socket, ArrayList<ServerThread> threads) {
        this.socket = socket;
        this.threadList = threads;
    }

    @Override
    public void run() {
        DBConnector dbHandler = new DBConnector();

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
                    if (userInput.split(",").length == 3){
                        int userPort = socket.getPort();
                        String[] tokens = userInput.split(",");
                        userName = tokens[0].split(":")[1];
                        String userPassword = tokens[1].split(":")[1];
                        boolean userFirstTimeLoggedIn = Boolean.parseBoolean(tokens[2].split(":")[1]);

                        if (userFirstTimeLoggedIn){
                            status = "online";
                            busy = false;
                            dbHandler.insertUserToDB(userPort, userName, userPassword, status, busy);
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

                    } else {
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

    private void printToALlClients(String outputString) {
        for(ServerThread st: threadList) {
            if (st.socket == socket){
            } else {
                st.output.println(outputString);
            }
        }
    }
}