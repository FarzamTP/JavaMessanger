import java.io.*;
import java.net.*;

public class Server{
    public static void main(String[] args) throws IOException
    {
        int port = 9999;
        ServerSocket server = new ServerSocket(port);
        System.out.println("Server running on localhost using port: " + port);

        while (true){
            Socket client = null;

            try{
                client = server.accept();
                System.out.println("A new client is connected : " + client);
                DataInputStream input = new DataInputStream(client.getInputStream());
                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                System.out.println("Assigning new thread for this client");
                Thread t = new ClientHandler(client, input, out);
                t.start();
            }
            catch (Exception e){
                client.close();
                e.printStackTrace();
            }
        }
    }
}