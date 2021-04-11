import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class ClientHandler extends Thread{

    final DataInputStream input;
    final DataOutputStream out;
    final Socket socket;

    public ClientHandler(Socket socket, DataInputStream input, DataOutputStream out){
        this.socket = socket;
        this.input = input;
        this.out = out;
    }

    @Override
    public void run(){
        String received;

        try {
            out.writeUTF("Welcome to our server!\nSend me your messages.");
        } catch (IOException e) {
            e.printStackTrace();
        }


        while (true){
            try {
                received = input.readUTF();

                System.out.println("Client with port no. " + this.socket.getPort() + " sent: " + received);

                if(received.equalsIgnoreCase("Exit")){
                    System.out.println("Client " + this.socket + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.socket.close();
                    System.out.println("Connection closed");
                    break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try{
            this.input.close();
            this.out.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}