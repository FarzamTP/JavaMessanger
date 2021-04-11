import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public Server(int port){
        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("Server has been started!");
            System.out.println("Waiting for clients to connect...");
            Socket socket = server.accept();
            System.out.println("Client connected!");
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            String line = "";
            while (!line.equals("exit")){
                line = in.readUTF();
                System.out.println("Client sent: " + line);
            }
            System.out.println("Client disconnected.");
            socket.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server(9999);
    }

}
