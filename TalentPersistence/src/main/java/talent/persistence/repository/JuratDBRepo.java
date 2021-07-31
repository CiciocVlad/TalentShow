package talent.persistence.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import talent.model.Jurat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
@Primary
public class JuratDBRepo implements IJurat {
    private final JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public JuratDBRepo(Properties props) {
        logger.info("Initializing with props: {}", props);
        this.dbUtils = new JdbcUtils(props);
    }

    @Override
    public Jurat findOne(Long id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        Jurat jurat = new Jurat();
        try (PreparedStatement preparedStatement = con.prepareStatement("select * from Jurati where id = ?")) {
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();
            String username = result.getString("username");
            String password = result.getString("password");
            jurat = new Jurat(username, password);
            jurat.setId(id);
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.printf("Error DB %s\n", e);
        }
        logger.traceExit(jurat);
        return jurat;
    }

    @Override
    public Iterable<Jurat> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Jurat> jurati = new ArrayList<>();

        try (PreparedStatement preparedStatement = con.prepareStatement("select * from Jurati")) {
            try (ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    Long id = result.getLong("id");
                    String username = result.getString("username");
                    String password = result.getString("password");
                    Jurat jurat = new Jurat(username, password);
                    jurat.setId(id);
                    jurati.add(jurat);
                }
            }
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.printf("Error DB %s\n", e);
        }
        logger.traceExit(jurati);
        return jurati;
    }

    @Override
    public void save(Jurat jurat) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = con.prepareStatement("insert into Jurati(username, password) values(?, ?)")) {
            preparedStatement.setString(1, jurat.getUsername());
            preparedStatement.setString(2, jurat.getPassword());
            int result = preparedStatement.executeUpdate();
            logger.trace("saved {} instances", result);
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.printf("Error DB %s\n", e);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Long id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = con.prepareStatement("delete from Jurati where id = ?")) {
            preparedStatement.setLong(1, id);
            int result = preparedStatement.executeUpdate();
            logger.trace("deleted {} instances", result);
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.printf("Error DB %s\n", e);
        }
        logger.traceExit();
    }

    @Override
    public Jurat findByUsername(String username) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        Jurat jurat = new Jurat();

        try (PreparedStatement preparedStatement = con.prepareStatement("select * from Jurati where username = ?")) {
            preparedStatement.setString(1, username);
            ResultSet result = preparedStatement.executeQuery();
            String password = result.getString("password");
            Long id = result.getLong("id");
            jurat = new Jurat(username, password);
            jurat.setId(id);
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.printf("Error DB %s\n", e);
        }
        logger.traceExit(jurat);
        return jurat;
    }

    @Override
    public void update(Jurat jurat) { }
}
