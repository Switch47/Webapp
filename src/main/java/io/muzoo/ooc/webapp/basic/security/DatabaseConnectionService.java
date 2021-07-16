package io.muzoo.ooc.webapp.basic.security;

import com.zaxxer.hikari.HikariDataSource;
import io.muzoo.ooc.webapp.basic.config.ConfigProperties;
import io.muzoo.ooc.webapp.basic.config.ConfigurationLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * We will make this singleton too
 */
public class DatabaseConnectionService {

    private final HikariDataSource ds;

    private static DatabaseConnectionService service;

    /**
     * Database connection pool using hikari library
     * The secret and variables are loaded from disk
     * The file config. properties is not committed to git repository
     */

    public DatabaseConnectionService() {
        ds = new HikariDataSource();
        ds.setMaximumPoolSize(20);
        ConfigProperties configProperties = ConfigurationLoader.load();
        if (configProperties == null) {
            throw new RuntimeException("Unable to read the config.properties");
        }
        ds.setDriverClassName(configProperties.getDatabaseDriverClassName());
        ds.setJdbcUrl(configProperties.getDatabaseConnectionUrl());
        ds.addDataSourceProperty("user", configProperties.getDatabaseUsername());
        ds.addDataSourceProperty("password", configProperties.getDatabasePassword());
        ds.setAutoCommit(false);
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static DatabaseConnectionService getInstance() {
        if (service == null) {
            service = new DatabaseConnectionService();

        }
        return service;
    }

//    private static DatabaseConnectionService service;
//
//    public Connection getConnection() {
//        try {
//            ConfigProperties configProperties = ConfigurationLoader.load();
//
//            if (configProperties == null) {
//                throw new RuntimeException("Unable to read the config.properties");
//            }
//
//            String jdbcDriver = configProperties.getDatabaseDriverClassName();
//            String jdbcURL = configProperties.getDatabaseConnectionUrl();
//            String username = configProperties.getDatabaseUsername();
//            String password = configProperties.getDatabasePassword();
//            Class.forName(jdbcDriver);
//
//            Connection connection = DriverManager.getConnection(jdbcURL,username,password);
//            return connection;
//
//        } catch (SQLException | ClassNotFoundException throwables) {
//            return null;
//        }
//    }
//
//    public static DatabaseConnectionService getInstance() {
//        if (service == null) {
//            service = new DatabaseConnectionService();
//        }
//        return service;
//    }

}
