package DatabaseConfiguration;

import java.sql.*;

import static DatabaseConfiguration.Config.*;

public class DBConnection {
    public static Connection getConnection() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(DATABASE_URL,DATABASE_USERNAME,DATABASE_PASSWORD);
        return connection;
    }
}
