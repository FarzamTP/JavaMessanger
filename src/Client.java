import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public Client(String address, int port) throws IOException {
        Socket socket = new Socket(address, port);
        System.out.println("Connected to server: " + address + " with port: " + port);
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        Scanner sc = new Scanner(System.in);

        String line = "";
        while (!line.equals("exit")){
//            System.out.println(input.readUTF());
            System.out.print("$ ");
            line = sc.nextLine();
//            System.out.println("You sent: " + line);
            out.writeUTF(line);

            if(line.equalsIgnoreCase("Exit")){
                System.out.println("Closing this connection : " + socket);
                socket.close();
                System.out.println("Connection closed");
                break;
            }
        }
        sc.close();
        input.close();
        out.close();
    }

    public static void main(String[] args) throws IOException {
        new Client("localhost", 9999);
    }
}
