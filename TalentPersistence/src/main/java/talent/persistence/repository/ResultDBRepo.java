package talent.persistence.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import talent.model.Concurent;
import talent.model.Jurat;
import talent.model.Result;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class ResultDBRepo implements IResult {
    private final JdbcUtils dbUtils;
    private final ConcurentDBRepo concurentDBRepo;
    private final JuratDBRepo juratDBRepo;

    private static final Logger logger = LogManager.getLogger();

    public ResultDBRepo(Properties props) {
        logger.info("initializing Result DB repo with props: {}", props);
        this.dbUtils = new JdbcUtils(props);
        this.concurentDBRepo = new ConcurentDBRepo(props);
        this.juratDBRepo = new JuratDBRepo(props);
    }

    @Override
    public Result findOne(Long id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        Result res = new Result();

        try (PreparedStatement preparedStatement = con.prepareStatement("select * from Result where id = ?")) {
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();
            Long idConcurent = result.getLong("idConcurent");
            Long idJurat = result.getLong("idJurat");
            String r = result.getString("result");
            res = new Result(concurentDBRepo.findOne(idConcurent), juratDBRepo.findOne(idJurat), r);
            res.setId(id);
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.printf("Error DB %s\n", e);
        }
        logger.traceExit(res);
        return res;
    }

    @Override
    public Iterable<Result> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Result> resultList = new ArrayList<>();

        try (PreparedStatement preparedStatement = con.prepareStatement("select * from Result")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    Concurent concurent = concurentDBRepo.findOne(resultSet.getLong("idConcurent"));
                    Jurat jurat = juratDBRepo.findOne(resultSet.getLong("idJurat"));
                    String res = resultSet.getString("result");
                    Result result = new Result(concurent, jurat, res);
                    result.setId(id);
                    resultList.add(result);
                }
            }
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.printf("Error DB %s\n", e);
        }
        logger.traceExit(resultList);
        return resultList;
    }

    @Override
    public void save(Result result) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = con.prepareStatement("insert into Result(idConcurent, idJurat, result) values(?, ?, ?)")) {
            preparedStatement.setLong(1, result.getConcurent().getId());
            preparedStatement.setLong(2, result.getJurat().getId());
            preparedStatement.setString(3, result.getResult());
            int count = preparedStatement.executeUpdate();
            logger.trace("saved {} instances", count);
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

        try (PreparedStatement preparedStatement = con.prepareStatement("delete from Result where id = ?")) {
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
    public Iterable<Result> searchByConcurentId(Long id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Result> resultList = new ArrayList<>();

        try (PreparedStatement preparedStatement = con.prepareStatement("select * from Result where idConcurent = ?")) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Long idResult = resultSet.getLong("id");
                    Long idJurat = resultSet.getLong("idJurat");
                    String resultValue = resultSet.getString("result");
                    Result finalResult = new Result(concurentDBRepo.findOne(id), juratDBRepo.findOne(idJurat), resultValue);
                    finalResult.setId(idResult);
                    resultList.add(finalResult);
                }
            }
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.printf("Error DB %s\n", e);
        }
        logger.traceExit(resultList);
        return resultList;
    }

    @Override
    public Iterable<Result> searchByJuratId(Long id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Result> resultList = new ArrayList<>();

        try (PreparedStatement preparedStatement = con.prepareStatement("select * from Result where idJurat = ?")) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Long idResult = resultSet.getLong("id");
                    Long idConcurent = resultSet.getLong("idConcurent");
                    String resultValue = resultSet.getString("result");
                    Result finalResult = new Result(concurentDBRepo.findOne(idConcurent), juratDBRepo.findOne(id), resultValue);
                    finalResult.setId(idResult);
                    resultList.add(finalResult);
                }
            }
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.printf("Error DB %s\n", e);
        }
        logger.traceExit(resultList);
        return resultList;
    }

    @Override
    public void update(Result result) { }
}
