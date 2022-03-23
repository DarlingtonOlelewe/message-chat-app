package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class InterComupterServer {
    private ServerSocket serverSocket;

    public InterComupterServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    public void startServer(){
        try {
            while(!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected");

                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();

            }

        }catch (IOException e){
            closeServerSocket();
//            e.printStackTrace();
        }
    }

    public void closeServerSocket(){
        try {
            if(serverSocket != null){
                serverSocket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
