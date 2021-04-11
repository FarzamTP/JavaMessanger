import java.io.*;
import java.net.Socket;

public class Client {

    public Client(String address, int port) throws IOException {
        Socket socket = new Socket(address, port);
        System.out.println("Connected to server: " + address + " with port: " + port);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        String line = "";
        while (!line.equals("exit")){
            line = reader.readLine();
            out.writeUTF(line);
        }
        reader.close();
        out.close();
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        new Client("localhost", 9999);
    }

}
