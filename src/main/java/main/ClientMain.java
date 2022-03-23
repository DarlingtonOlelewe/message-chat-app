package main;

import server.Client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter your username ");
        String username = input.nextLine();

        Socket socket = new Socket("localhost",1234);
        Client client = new Client(socket,username);
        client.listenForMessage();
        client.sendMessage();
    }
}
