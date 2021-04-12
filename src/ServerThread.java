import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;


public class ServerThread extends Thread {
    private Socket socket;
    private ArrayList<ServerThread> threadList;
    private PrintWriter output;
    private String userName;
    private String status;

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
                    String message = userName + " left the server!";
                    System.out.println(message);
                    printToALlClients(message);
                    break;
                } else {
                    if (userInput.split(",").length == 3){
                        DBConnector dbHandler = new DBConnector();

                        int userPort = socket.getPort();
                        String[] tokens = userInput.split(",");
                        userName = tokens[0].split(":")[1];
                        String userPassword = tokens[1].split(":")[1];
                        boolean userFirstTimeLoggedIn = Boolean.parseBoolean(tokens[2].split(":")[1]);

                        if (userFirstTimeLoggedIn){
                            dbHandler.insertUserToDB(userPort, userName, userPassword);
                            String message = "User " + userName + " joined us!";
                            System.out.println(message);
                            printToALlClients(message);
                            output.println("Welcome to the server!\nYou can leave server by sending 'exit'.");
                        } else {
                            if (dbHandler.authenticateUser(userName, userPassword)){
                                String message = "User " + userName + " has logged in!";
                                System.out.println(message);
                                printToALlClients(message);
                                output.println("Welcome to the server!\nYou can leave server by sending 'exit'.");
                            } else {
                                System.out.println("User " + userName + " failed to log in.");
                                output.println("Password incorrect. Authentication failed!\nPlease try again later.");
                                socket.close();
                                break;
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