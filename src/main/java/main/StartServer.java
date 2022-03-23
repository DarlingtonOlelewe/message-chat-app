package main;

import server.InterComupterServer;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class StartServer {
    public static void main(String[] args) throws IOException {
        InterComupterServer interComupterServer = new InterComupterServer(1234);
        interComupterServer.startServer();
    }
}
