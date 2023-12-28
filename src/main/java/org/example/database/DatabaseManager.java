package org.example.database;

import org.example.characterPolicy;
import org.example.returnStatus;
import org.example.userPackage;

import java.io.IOException;
import java.sql.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/databasetest";
    private static final String USER = "root";
    private static final String PASSWORD = "";


    public static void initializeDatabase(){
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {

            String sqlCreateUserTable = "CREATE TABLE IF NOT EXISTS uzytkownicy (" +
                    "id_uzytkownik INT AUTO_INCREMENT PRIMARY KEY," +
                    "nazwa_uzytkownika VARCHAR(50)," +
                    "email VARCHAR(100)," +
                    "haslo VARCHAR(255)," +
                    "data_rejestracji DATETIME," +
                    "rola VARCHAR(20)" +
                    ")";
            statement.executeUpdate(sqlCreateUserTable);

            String sqlCreateBusesTable = "CREATE TABLE IF NOT EXISTS linie_autobusowe (" +
                    "id_linii INT AUTO_INCREMENT PRIMARY KEY," +
                    "nazwa_linii VARCHAR(50)," +
                    "opis TEXT" +
                    ")";
            statement.executeUpdate(sqlCreateBusesTable);

            String sqlCreateBusStopTable = "CREATE TABLE IF NOT EXISTS przystanki (" +
                    "id_przystanku INT AUTO_INCREMENT PRIMARY KEY," +
                    "nazwa_przystanku VARCHAR(100)," +
                    "lokalizacja VARCHAR(255)" +
                    ")";
            statement.executeUpdate(sqlCreateBusStopTable);

            String sqlCreateScheduleTable = "CREATE TABLE IF NOT EXISTS rozklad_jazdy (" +
                    "id_rozkladu INT AUTO_INCREMENT PRIMARY KEY," +
                    "id_linii INT," +
                    "id_przystanku INT," +
                    "czas_odjazdu TIME" +
                    ")";
            statement.executeUpdate(sqlCreateScheduleTable);

            String sqlCreateRoutesTable = "CREATE TABLE IF NOT EXISTS trasy (" +
                    "id_trasy INT AUTO_INCREMENT PRIMARY KEY," +
                    "id_linii INT," +
                    "nazwa_trasy VARCHAR(100)" +
                    ")";
            statement.executeUpdate(sqlCreateRoutesTable);

            String sqlCreateRoutesBusStopTable = "CREATE TABLE IF NOT EXISTS trasa_przystanek (" +
                    "id_trasy_przystanku INT AUTO_INCREMENT PRIMARY KEY," +
                    "id_trasy INT," +
                    "id_przystanku INT," +
                    "kolejnosc INT" +
                    ")";
            statement.executeUpdate(sqlCreateRoutesBusStopTable);

            String sqlCreateTicketTable = "CREATE TABLE IF NOT EXISTS bilety (" +
                    "id_biletu INT AUTO_INCREMENT PRIMARY KEY," +
                    "id_uzytkownika INT," +
                    "id_rozkladu INT," +
                    "data_waznosci DATETIME," +
                    "cena DECIMAL(10,2)" +
                    ")";
            statement.executeUpdate(sqlCreateTicketTable);

            String sqlCreateHistoryTable = "CREATE TABLE IF NOT EXISTS historia_zakupu (" +
                    "id_zakupu INT AUTO_INCREMENT PRIMARY KEY," +
                    "id_uzytkownika INT," +
                    "id_biletu INT," +
                    "data_zakupu DATETIME," +
                    "metoda_platnosci VARCHAR(50)" +
                    ")";
            statement.executeUpdate(sqlCreateHistoryTable);

            //String sqlInsertData = "INSERT INTO przykladowa_tabela (nazwa, wartosc) VALUES ('Przykład', 100)";
            //statement.executeUpdate(sqlInsertData);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void executeQueryFromFile(String file_path)
    {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
                String sqlInsertStatement = null;
                List<String> inputLines = Files.readAllLines(Paths.get(file_path));
                String[] inputWords = new String[100];
                for(String line : inputLines) {
                    sqlInsertStatement = line;
                    statement.executeUpdate(sqlInsertStatement);
                }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean findUser(userPackage user)
    {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement())
        {
            String sqlFindUser = "SELECT nazwa FROM uzytkownicy WHERE nazwa = "+user.getName();
            ResultSet resultSet = statement.executeQuery(sqlFindUser);

            if (!resultSet.next()) {
                return false;
            }
            sqlFindUser = "SELECT email FROM uzytkownicy WHERE email = "+user.getEmail();
            resultSet = statement.executeQuery(sqlFindUser);

            if(!resultSet.next())
                return false;

            return true;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createUser(userPackage user)
    {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement())
        {
            returnStatus returnstatus = null;
            if(!user.checkConfirmEmail() || !user.checkConfirmPassword())
            {
                return;
            }

            characterPolicy charPolicy = new characterPolicy(user.getPassword());
            int policyStatus = characterPolicy.password_Validation();

            switch(policyStatus)
            {
                case 0:
                    returnstatus = new returnStatus(false,"Missing special character or invalid character was used");
                    break;
                case 1:
                    returnstatus = new returnStatus(true,"");
                    break;
                case -1:
                    returnstatus = new returnStatus(false,"Password is too short. It must be withing range of 8-25");
                    break;
                case -2:
                    returnstatus = new returnStatus(false,"Password is too long. It must be withing range of 8-25");
                    break;
                default:
                    returnstatus = new returnStatus(false,"Something went wrong, please try again.");
                    break;
            }

            if(!returnstatus.getStatus())
            {
                //blad - przerwanie wykonywania
                //wysyłanie tego do klienta
                return;
            }

            if(!findUser(user))
            {
                String sqlCreateUser = "INSERT INTO uzytkownicy (nazwa_uzytkownika,email,haslo,data_rejestracji,rola) VALUES ("
                        +user.getName()+","
                        +user.getEmail()+","
                        +user.getPassword()+","
                        +"NOW()"
                        +"Klient";
                statement.executeQuery(sqlCreateUser);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void checkData(){
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            String sqlQuery = "SELECT * FROM przykladowa_tabela";
            ResultSet resultSet = statement.executeQuery(sqlQuery);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nazwa = resultSet.getString("nazwa");
                String data = resultSet.getString("data_wprowadzenia");
                System.out.println("ID: " + id + ", Nazwa: " + nazwa + ", Data: " + data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL,USER,PASSWORD);
    }

}
