package talent.persistence.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {
    private Properties jdbcProps;

    private static final Logger logger = LogManager.getLogger();

    public JdbcUtils(Properties props) {
        this.jdbcProps = props;
    }

    private Connection instance = null;

    private Connection getNewConnection() {
        logger.traceEntry();

        String url = jdbcProps.getProperty("talent.jdbc.url");
        String user = jdbcProps.getProperty("jdbc.user");
        String pass = jdbcProps.getProperty("jdbc.pass");
        logger.info("connecting to database ... {}", url);
        logger.info("user: {}", user);
        logger.info("pass: {}", pass);
        Connection conn = null;
        try {
            if (user != null && pass != null)
                conn = DriverManager.getConnection(url, user, pass);
            else
                conn = DriverManager.getConnection(url);
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.printf("Error Connecting to DB %s\n", e);
        }
        return conn;
    }

    public Connection getConnection() {
        logger.traceEntry();
        try {
            if (instance == null || instance.isClosed())
                instance = getNewConnection();
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.printf("Error DB %s\n", e);
        }
        logger.traceExit(instance);
        return instance;
    }
}
