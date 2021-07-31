package talent.persistence.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import talent.model.Concurent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class ConcurentDBRepo implements IConcurent {
    private final JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public ConcurentDBRepo(Properties props) {
        logger.info("Initializing Concurent Repo with props: {}", props);
        this.dbUtils = new JdbcUtils(props);
    }

    @Override
    public Concurent findOne(Long id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        Concurent concurent = new Concurent();

        try (PreparedStatement preparedStatement = con.prepareStatement("select * from Concurenti where id = ?")) {
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();
            String name = result.getString("name");
            String res = result.getString("result");
            concurent = new Concurent(name, res);
            concurent.setId(id);
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.printf("Error DB %s\n", e);
        }
        logger.traceExit(concurent);
        return concurent;
    }

    @Override
    public Iterable<Concurent> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Concurent> concurenti = new ArrayList<>();

        try (PreparedStatement preparedStatement = con.prepareStatement("select * from Concurenti")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String res = resultSet.getString("result");
                    Long id = resultSet.getLong("id");
                    Concurent concurent = new Concurent(name, res);
                    concurent.setId(id);
                    concurenti.add(concurent);
                }
            }
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.printf("Error DB %s\n", e);
        }
        logger.traceExit(concurenti);
        return concurenti;
    }

    @Override
    public void save(Concurent concurent) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preparedStatement = con.prepareStatement("insert into Concurenti(name, result) values(?, ?)")) {
            preparedStatement.setString(1, concurent.getName());
            preparedStatement.setString(2, concurent.getResult());
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

        try (PreparedStatement preparedStatement = con.prepareStatement("delete from Concurenti where id = ?")) {
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
    public void update(Concurent concurent) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = con.prepareStatement("update Concurenti set result = ? where id = ?")) {
            preparedStatement.setString(1, concurent.getResult());
            preparedStatement.setLong(2, concurent.getId());
            int result = preparedStatement.executeUpdate();
            logger.trace("updated {} instances", result);
        }
        catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        logger.traceExit();
    }

    @Override
    public Concurent findByName(String name) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        Concurent concurent = new Concurent();

        try (PreparedStatement preparedStatement = con.prepareStatement("select * from Concurenti where name = ?")) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            Long id = resultSet.getLong("id");
            String result = resultSet.getString("result");
            concurent = new Concurent(name, result);
            concurent.setId(id);
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.printf("Error DB %s\n", e);
        }
        logger.traceExit(concurent);
        return concurent;
    }
}
