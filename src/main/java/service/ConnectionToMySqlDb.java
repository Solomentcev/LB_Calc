package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionToMySqlDb {
    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);
    private static final Properties dbProps = new Properties();
    private static Connection connection;

    public ConnectionToMySqlDb(){}

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            try {
                dbProps.load(new FileInputStream("src/main/resources/db.properties"));
            } catch (IOException e) {
                logger.error("Не удалось загрузить настройки БД " + String.valueOf(e));
                throw new RuntimeException(e);
            }
        }
            try {
                connection = DriverManager.getConnection(
                        dbProps.getProperty("url"),
                        dbProps.getProperty("user"),
                        dbProps.getProperty("password"));
            } catch (SQLException e) {
                logger.error("Не удалось подключиться к БД " + String.valueOf(e));
                throw new SQLException(e);
            }

        return connection;
    }
}
