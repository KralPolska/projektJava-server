package org.example.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
public class Server {
    private int port;

    public Server(int port){
        this.port = port;
    }

    public void start(){
        try(ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Serwer uruchomiony na porcie: "+port);

            while (true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nowe polaczenie przyjete");
                new Thread(new ClientHandler(clientSocket)).start();

            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
