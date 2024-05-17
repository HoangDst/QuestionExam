package DatabaseConfiguration;

import java.sql.*;
import static DatabaseConfiguration.Config.*;
import java.util.List;
public class DBConnector {
    private Connection connection;
    public DBConnector() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DATABASE_URL,DATABASE_USERNAME,DATABASE_PASSWORD);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() { return connection; }


    public void execution(String query) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void execution(String query, List<Object> parameters) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
