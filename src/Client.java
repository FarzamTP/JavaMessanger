import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String userInput = "";
        String userName = "None";

        try{
            Socket socket = new Socket("localhost", 9999);
            System.out.println("Connected to server localhost with port 9999");

            BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(),true);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            ClientRunnable clientRun = new ClientRunnable(socket);

            new Thread(clientRun).start();

            while (!userInput.equalsIgnoreCase("Exit")){
                if (userName.equals("None")) {
                    System.out.print("Enter your username: ");
                    userInput = scanner.nextLine();
                    System.out.println("Welcome to the server!\nYou can leave server by sending 'exit'.");
                    userName = userInput;
                    writer.write("Username:" + userName + "\n");
                }
                else {
                    userInput = scanner.nextLine();
                    writer.write(userInput + "\n");
                }
                writer.flush();
            }
            System.out.println("Disconnected from server.\nBye " + userName + "!");
            writer.close();
            System.exit(0);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}