package interfaces;

import workers.Worker;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class SQLSender implements ConnectTo {

    public void insertWorker(Worker worker) { //Добавление сотрудника в базу
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                Statement statement = conn.createStatement();
                StringBuilder set = new StringBuilder();
                set.append("INSERT workers (id, workername, post, paymentPerDay, paymentPerHour, peymentForHollydays) VALUES" +
                        " ('" + worker.getName().hashCode() + "', '" + worker.getName() + "', " + worker.getPost() +
                        ", '" + worker.getPaymentPerDay() + "', '" + worker.getPaymentPerHour() + "', '"
                        + worker.getPeymentForHollydays() + "') ON DUPLICATE KEY UPDATE workername= '" + worker.getName()
                        + "', post = " + worker.getPost() + ", paymentPerDay = '" + worker.getPaymentPerDay() + "', " +
                        "paymentPerHour = '" + worker.getPaymentPerHour() + "', peymentForHollydays = '"
                        + worker.getPeymentForHollydays() + "';");
                statement.executeUpdate(String.valueOf(set));
                statement.close();



            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createWorkerDB() {   //Создание базы сотрудников
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                Statement statement = conn.createStatement();
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS workers " +
                        "(id VARCHAR(100) PRIMARY KEY ," +
                        "workername VARCHAR(255) NOT NULL," +
                        "post BOOLEAN DEFAULT false," +
                        "paymentPerDay DOUBLE," +
                        "paymentPerHour DOUBLE," +
                        "peymentForHollydays DOUBLE);");
                statement.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createMonthDB(String month) {  //создание базы месячной статистики
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();

            try (Connection conn = getConnection()) {
                Statement statement = conn.createStatement();
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS `" + month + "` " +
                        "(id VARCHAR(100) PRIMARY KEY UNIQUE, " +
                        "workdays INT, " +
                        "workholydays INT, " +
                        "wage DOUBLE, " +
                        "elabortime DOUBLE, " +
                        "elaborwage DOUBLE, " +
                        "fullwage DOUBLE," +
                        "FOREIGN KEY (id) " +
                        "REFERENCES workers (id) ON DELETE CASCADE);");
                statement.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList selectAllWorkersWithMonthStat(String month) {  //Выбор всех сотрудников из месячной статистики
        ArrayList result = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("select * from " + month + " LEFT join workers USING " +
                        "(id) ORDER BY workername;");

                while (resultSet.next()) {
                    Worker worker = new Worker(resultSet.getString(8), resultSet.getBoolean(9),
                            resultSet.getDouble(10), resultSet.getDouble(11),
                            resultSet.getDouble(12));
                    worker.setWorkDays(resultSet.getInt(2));
                    worker.setWorkHolydays(resultSet.getInt(3));
                    worker.setWage(resultSet.getDouble(4));
                    worker.setElaborTimes(resultSet.getDouble(5));
                    worker.setWageElaborTime(resultSet.getDouble(6));
                    worker.setFullWage(resultSet.getDouble(7));
                    result.add(worker);
                }
                statement.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void insertMonthValue(Worker worker, String month) { //Добавление значений в месячную базу

        createMonthDB(month);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                Statement statement = conn.createStatement();
                statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 0;");
                statement.executeUpdate("INSERT INTO `" + month + "`(id, workdays, workholydays, wage, elabortime, " +
                        "elaborwage, fullwage) VALUES (" + worker.getName().hashCode() + ", " + worker.getWorkDays() +
                        ", " + worker.getWorkHolydays() + ", " + worker.getWage() + ", " + worker.getElaborTimes() +
                        ", " + worker.getWageElaborTime() + ", " + worker.getFullWage() + ") ON DUPLICATE KEY UPDATE " +
                        "workdays = " + worker.getWorkDays() + ", workholydays = " + worker.getWorkHolydays() +
                        ", wage = " + worker.getWage() + ", elabortime = " + worker.getElaborTimes() +
                        ", elaborwage = " + worker.getWageElaborTime() + ", fullwage = " + worker.getFullWage() + ";");
                statement.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {  //создание соединения с MySQL

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

    public void createSchema() { //создание схемы
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                Statement statement = conn.createStatement();
                statement.executeUpdate("CREATE SCHEMA IF NOT EXISTS `Inbulk`;");
                statement.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList selectAllWorkers() { //Вывод всех сотрудников
        ArrayList workers = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("select * from workers;");
                while (resultSet.next()) {
                    Worker worker = new Worker(resultSet.getString(2), resultSet.getBoolean(3),
                            resultSet.getDouble(4), resultSet.getDouble(5),
                            resultSet.getDouble(6));
                    workers.add(worker);
                }
                statement.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workers;
    }

    public void deleteTable(String name) {  //Удаление таблицы
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                Statement statement = conn.createStatement();
                statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 0;");
                statement.executeUpdate("DROP TABLES " + name + ";");
                statement.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Worker selectWorker(String name) { //Проверк наличие сотрудник в базе

        Worker worker = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("select * from workers WHERE workername = '" + name + "';");
                resultSet.next();
                worker = new Worker(resultSet.getString(2), resultSet.getBoolean(3),
                        resultSet.getDouble(4), resultSet.getDouble(5),
                        resultSet.getDouble(6));
                statement.close();
                }
            } catch (SQLException | ClassNotFoundException e) {

                return null;

            } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return worker;
    }

    public void deleteWorker(String name) {  //Удаление таблицы
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                Statement statement = conn.createStatement();
                statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 0;");
                statement.executeUpdate("DELETE FROM workers WHERE workername = '" + name + "';");
                statement.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editWorker(String name, Boolean post, String paymentPerDay, String paymentPerHour,
                           String peymentForHollydays) {  //Редактирование сотрудника
        Worker worker = selectWorker(name);
        if (paymentPerDay == null){
            paymentPerDay = String.valueOf(worker.getPaymentPerDay());
        }
        if (paymentPerHour == null){
            paymentPerHour = String.valueOf(worker.getPaymentPerHour());
        }
        if (peymentForHollydays == null){
            paymentPerHour = String.valueOf(worker.getPeymentForHollydays());
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                Statement statement = conn.createStatement();
                statement.executeUpdate("UPDATE workers SET post = " + post + ", paymentPerDay = "
                        + Double.parseDouble(paymentPerDay) + ",  paymentPerHour = "
                        + Double.parseDouble(paymentPerHour) + ", peymentForHollydays = " +
                        "" + Double.parseDouble(peymentForHollydays) + " WHERE workername = '" + name + "';");
                statement.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}





