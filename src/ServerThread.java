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
                    System.out.println(userName + " left the server!");
                    break;
                } else {
                    if (userInput.split(":").length == 2){
                        userName = userInput.split(":")[1];
                        String message = "User " + userName + " joined!";
                        System.out.println(message);
                        printToALlClients(message);
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
            if (st.socket == this.socket){
//                st.output.println("You: " + outputString);
            } else {
                st.output.println(outputString);
            }
        }
    }
}