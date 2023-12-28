package org.example;

import org.example.database.DatabaseManager;
import org.example.server.Server;

public class Main {
    public static void main(String[] args)
    {
        int port = 1234;
        Server server = new Server(port);
        DatabaseManager.initializeDatabase();
        DatabaseManager.executeQueryFromFile("C:\\Users\\Ja\\Downloads\\serverProjectJava-master(1)\\serverProjectJava-master\\src\\main\\java\\org\\example\\database\\insert.txt");
        DatabaseManager.checkData();
        server.start();
    }
}