package interfaces;

import workers.Worker;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class SQLSender {

    public void insertWorker(ArrayList<Worker> workers) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                System.out.println("Соединение с базой установлено");
                Statement statement = conn.createStatement();
                for (Worker worker : workers) {
                    StringBuilder set = new StringBuilder();
                    if (worker.getName().hashCode() < 0) {
                        String hashCode = String.valueOf(worker.getName().hashCode() * (-1));
                        set.append("INSERT workers (id, name, post) VALUES ('" + hashCode + "', '" + worker.getName() + "'" +
                                ", '" + worker.getPost() + "');");
                    } else {
                        set.append("INSERT workers (id, name, post) VALUES ('" + worker.getName().hashCode() + "', '" + worker.getName() + "'" +
                                ", '" + worker.getPost() + "');");
                    }
                    statement.executeUpdate(String.valueOf(set));
                }
                statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException, IOException {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get("src/main/resources/files/database.pr"))) {
            props.load(in);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        String url = props.getProperty("url");
        String username = props.getProperty("username");
        String password = props.getProperty("password");
        return DriverManager.getConnection(url, username, password);
    }

    public void createWorkerDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                System.out.println("Соединение с базой установлено");
                Statement statement = conn.createStatement();
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS workers " +
                        "(id VARCHAR(100) PRIMARY KEY," +
                        "name VARCHAR(255) NOT NULL," +
                        "post VARCHAR(30)," +
                        "doublesalary BOOLEAN DEFAULT false);");
                statement.close();
                System.out.println("Таблица сотрудников создана успешно");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void createMonthDB(String month) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                System.out.println("Соединение с базой установлено");
                Statement statement = conn.createStatement();
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + month + " " +
                        "(id VARCHAR(100) PRIMARY KEY, " +
                        "workdays INT, " +
                        "wage DOUBLE, " +
                        "elabordays INT, " +
                        "elaborwage DOUBLE, " +
                        "FOREIGN KEY (id) " +
                        "REFERENCES workers (id));");
                statement.close();
                System.out.println("Таблица " + month + " создана успешно");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
