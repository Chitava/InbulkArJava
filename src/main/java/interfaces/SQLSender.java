package interfaces;
import workers.Worker;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class SQLSender implements ConnectTo {
    public void createWorkerDB() {              //Создание таблицы сотрудников
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                Statement statement = conn.createStatement();
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS workers " +
                        "(id VARCHAR(100) PRIMARY KEY," +
                        "name VARCHAR(255) NOT NULL," +
                        "post BOOLEAN DEFAULT false," +
                        "paymentPerDay DOUBLE," +
                        "paymentPerHour DOUBLE," +
                        "peymentForHollydays DOUBLE);");
                statement.close();
                System.out.println("Таблица сотрудников создана успешно");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void insertWorker(Worker worker) {    //Добавление записи в таблицу сотрудников
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                System.out.println("Соединение с базой установлено");
                Statement statement = conn.createStatement();
                StringBuilder set = new StringBuilder();
                set.append("INSERT workers (id, name, post, paymentPerDay, paymentPerHour, peymentForHollydays) " +
                            "VALUES ('" + worker.getName().hashCode() + "', '" + worker.getName() + "', "
                           + worker.getPost() + ", '" + worker.getPaymentPerDay()+ "', '" + worker.getPaymentPerHour()
                           + "', '" + worker.getPeymentForHollydays() + "');");
                statement.executeUpdate(String.valueOf(set));
                statement.close();
                System.out.println("Сотрудник добавлен успешно");
//            } catch (Exception e) {
//                e.printStackTrace();
            } catch (SQLIntegrityConstraintViolationException e){
                System.out.println("Сотрудник не может быть добавлен, дублирование записи");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void createMonthDB(String month) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                Statement statement = conn.createStatement();
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS `" + month + "` " +
                        "(id VARCHAR(100) PRIMARY KEY UNIQUE, " +
                        "workdays INT, " +
                        "holydaystime INT, " +
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

    public ArrayList selectAll(String month) {
        ArrayList result = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("select * from " + month + " LEFT join workers USING (id);");
                while (resultSet.next()) {
                    result.add(resultSet.getString(6));                                     /*Имя*/
                    result.add(resultSet.getString(7));                                     /*Должность*/
                    result.add(resultSet.getInt(2));                                        /*Рабочие дни*/
                    result.add(resultSet.getDouble(3));                                     /*ЗП за дни*/
                    result.add(resultSet.getDouble(4));                                     /*Вемя переработки*/
                    result.add(resultSet.getDouble(5));                                     /*ЗП за переработку*/
                    result.add(resultSet.getDouble(3) + resultSet.getDouble(5)); /*ЗП полная*/

                }
                statement.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void insertMonthValue(ArrayList value, String month) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = getConnection()) {
                System.out.println("Соединение с базой установлено");
                Statement statement = conn.createStatement();
                statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 0;");
                statement.executeUpdate("INSERT INTO `" + month + "` (id, workdays, holydaystime, wage, elabordays, elaborwage) VALUES" +
                        " (" + value.get(0) + ", " + value.get(1) + ", " + value.get(2) + ", " +
                        "" + value.get(3) + ", " + value.get(4) + ", " + value.get(5) + ") ON DUPLICATE KEY UPDATE " +
                        "workdays = " + value.get(1) + "workdays = " + value.get(2) + ", wage = " + value.get(3) + "," +
                        " elabordays = " + value.get(4) + ", elaborwage = " + value.get(5) + ";");
                statement.close();
                System.out.println("Данные за " + month + " внесены в таблицу успешно");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {

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
}

