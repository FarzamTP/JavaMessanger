import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {

    public static void main(String[] args) {
        ArrayList<ServerThread> threadList = new ArrayList<>();

        try {
            ServerSocket serversocket = new ServerSocket(9999);
            System.out.println("Started socket server.\nWaiting for client to connect.");
            while(true) {
                Socket socket = serversocket.accept();
                System.out.println("New client " + socket + " has been connected.");
                ServerThread serverThread = new ServerThread(socket, threadList);
                threadList.add(serverThread);
                serverThread.start();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}