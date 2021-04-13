import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String userInput = "";
        String userName = "None";
        String password;
        boolean firstTimeLoggedIn;

        try{
            Socket socket = new Socket("localhost", 9999);
            System.out.println("Connected to server localhost with port 9999");

            BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(),true);

            DBConnector dbHandler = new DBConnector();

            ClientRunnable clientRun = new ClientRunnable(socket);

            new Thread(clientRun).start();

            while (!userInput.equalsIgnoreCase("Exit")){
                if (userName.equals("None")) {
                    System.out.print("Enter your username: ");
                    userName = scanner.nextLine();
                    if (dbHandler.userExists(userName)){
                        System.out.println("Please enter your password:");
                        firstTimeLoggedIn = false;
                    } else {
                        System.out.println("Please set a password:");
                        firstTimeLoggedIn = true;
                    }
                    password = scanner.nextLine();
                    output.println("Username:" + userName + ",Password:" + password + ",FirstTimeLoggedIn:" + firstTimeLoggedIn);
                }
                else {
                    if (dbHandler.getUserBusy(userName)){
                        String chatName = dbHandler.getChatName(userName);
                        userInput = scanner.nextLine();
                        String finalUserInput = "ChatName:" + chatName + "|" + userInput;
                        output.println(finalUserInput);
                    }
                }
            }
            System.out.println("Disconnected from server.\nBye " + userName + "!");
            output.close();
            System.exit(0);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}