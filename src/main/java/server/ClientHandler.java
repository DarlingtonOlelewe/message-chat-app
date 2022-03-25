package server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<ClientHandler>();
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String clientName;
    public ClientHandler(Socket acceptedSocket){
        try {
            this.socket = acceptedSocket;
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientName = reader.readLine();

            clientHandlers.add(this);
            broadCast("Server: "+clientName+" has joined the Chat");
        }catch (IOException e){
            closeEverything(socket,reader,writer);
            e.printStackTrace();
        }
    }

    public void run() {
        String messageFromClient;
        while(socket.isConnected()){
            try {
                messageFromClient = reader.readLine();

                if(messageFromClient == null || messageFromClient.equalsIgnoreCase("exit")){
                    closeEverything(socket,reader,writer);
                    break;
                }else{
                    broadCast(clientName+": "+messageFromClient);
                }

            }catch (IOException e){
                removeClientHandler();
                closeEverything(socket,reader,writer);
                break;
            }
        }

    }

    public void broadCast(String message){
        for(ClientHandler clientHandler: clientHandlers){
            try{
                if(!clientHandler.clientName.equals(clientName)){
                    clientHandler.writer.write(message);
                    clientHandler.writer.newLine();
                    clientHandler.writer.flush();
                }

            }catch (IOException e){
                closeEverything(socket,reader,writer);
            }

        }
    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadCast("Server: "+clientName+" has left the chat");

    }

    public void closeEverything(Socket socket, BufferedReader reader, BufferedWriter writer){
        removeClientHandler();
        try {
            if (reader != null) {
                reader.close();
            }
            if(writer != null){
                writer.close();
            }
            if(socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
