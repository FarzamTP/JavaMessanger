import java.io.*;
import java.net.*;
import java.util.HashMap;

public class Server{
    public static int port = 9999;
    public static HashMap<Integer, Thread> clientHashMap = new HashMap<Integer, Thread>();

    public static void main(String[] args) throws IOException
    {
        ServerSocket server = new ServerSocket(port);
        System.out.println("Server running on localhost using port: " + port);

        while (true){
            Socket socket = null;

            try{
                socket = server.accept();
                System.out.println("A new client is connected : " + socket);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                System.out.println("Assigning new thread for this client");
                Thread t = new ClientHandler(socket, input, out);
                clientHashMap.put(socket.getPort(), t);
                t.start();
            }
            catch (Exception e){
                socket.close();
                e.printStackTrace();
            }
        }
    }
}