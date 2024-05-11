package config;

import config.ConfigDataBase;

import java.sql.*;

import static config.ConfigDataBase.*;

public class DBConnection {
    public static Connection getConnection() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(DATABASE_URL,DATABASE_USERNAME,DATABASE_PASSWORD);
        return connection;
    }
}
