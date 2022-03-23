package server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;
    private String userName;
    private volatile boolean isOnline;

    public Client(Socket socket, String userName){
        try {
            this.socket = socket;
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userName = userName;
            this.isOnline = true;
        }catch (IOException e){
            closeEverything(socket,reader,writer);
        }
    }

    public void sendMessage(){
        try{
            writer.write(userName);
            writer.newLine();
            writer.flush();

            Scanner input = new Scanner(System.in);

            while(isOnline){
                String messageTosend = input.nextLine();
                writer.write(userName+": "+messageTosend);
                writer.newLine();
                writer.flush();
                if(messageTosend == null || messageTosend.equalsIgnoreCase("exit")){
                    isOnline = false;
                    closeEverything(socket,reader,writer);

                }
            }
        }catch (IOException e){
            closeEverything(socket,reader,writer);
        }
    }

    public void listenForMessage(){
        new Thread(new Runnable() {
            public void run() {
                String messageFromGroupChat;

                while (isOnline){
                    try{
                        messageFromGroupChat = reader.readLine();
                        if(messageFromGroupChat == null){
                            isOnline = false;
                            break;
                        }else{
                            System.out.println(messageFromGroupChat);
                        }
                    }catch (IOException e){
                        closeEverything(socket,reader,writer);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket,BufferedReader reader,BufferedWriter writer){
        try{
            if(reader != null){
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
