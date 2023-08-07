package interfaces;
import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectTo {
    public Connection getConnection() throws SQLException;
}
