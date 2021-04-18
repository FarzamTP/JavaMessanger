import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    String userInput = "";
    String userName = "None";
    String password;
    boolean firstTimeLoggedIn;
    private static BufferedInputStream bis = null;
    private static ObjectOutputStream os = null;

    public Client(){
        try {
            socket = new Socket("localhost", 9999);
            System.out.println("Connected to server localhost with port 9999");

            BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(),true);

            DBConnector dbHandler = new DBConnector();

            ClientRunnable clientRun = new ClientRunnable(socket);

            new Thread(clientRun).start();

            Scanner scanner = new Scanner(System.in);

            while (true){
                if (userName.equals("None")) {
                    System.out.print("Enter your username: ");
                    userName = scanner.nextLine();
                    if (dbHandler.userExists(userName)){
                        System.out.print("Please enter your password: ");
                        firstTimeLoggedIn = false;
                    } else {
                        System.out.print("Please set a password: ");
                        firstTimeLoggedIn = true;
                    }
                    password = scanner.nextLine();
                    output.println("Username:" + userName + ",Password:" + password + ",FirstTimeLoggedIn:" + firstTimeLoggedIn);
                }
                else {
                    if (dbHandler.getUserBusy(userName)){
                        userInput = scanner.nextLine();

                        String chatName = dbHandler.getChatName(userName);
                        String finalUserInput = "ChatName:" + chatName + "|" + userInput;

                        if (userInput.startsWith("sendFile")){
                            String fileName = userInput.split(" ")[1];
                            File myFile = new File(fileName);
                            boolean fileExists = myFile.exists();
                            if (fileExists){
                                int fileSize = (int) myFile.length();
                                System.out.println("File exists!");
                                System.out.println("Attempting to read file " + fileName);
                                finalUserInput += (" " + fileSize);
                                readSendFile(fileName);
                            } else {
                                System.out.println("File doesn't exist!");
                            }
                        }
                        output.println(finalUserInput);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void readSendFile(String fileName) throws IOException {
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        FileInputStream fis = new FileInputStream(fileName);

        byte[] buffer = new byte[4096];

        while (fis.read(buffer) > 0) {
            dos.write(buffer);
        }

        fis.close();
        dos.close();
    }

    public static void main(String[] args) {
        new Client();
    }
}