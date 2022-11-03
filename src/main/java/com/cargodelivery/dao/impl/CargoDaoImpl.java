package com.cargodelivery.dao.impl;

import com.cargodelivery.dao.CargoDao;
import com.cargodelivery.dao.HikariCP;
import com.cargodelivery.dao.entity.Cargo;
import com.cargodelivery.exception.DBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class CargoDaoImpl implements CargoDao {

    private static final Logger LOG = LoggerFactory.getLogger(CargoDaoImpl.class);

    @Override
    public Cargo save(Cargo cargo) throws DBException {
        String addCargoSql = "INSERT INTO cargo VALUE (?, ?, ?, ?, ?);";
        String getLastCargoIdSql = "SELECT id FROM cargo ORDER BY id desc LIMIT 1;";
        int id = 0;
        int i = 0;
        try (var connection = HikariCP.getHikariConnection()) {
            try (var prepareStatement = connection.prepareStatement(addCargoSql)) {
                ResultSet resultSet = prepareStatement.executeQuery(getLastCargoIdSql);
                while (resultSet.next()) {
                    id = resultSet.getInt("id");
                }

                prepareStatement.setInt(++i, ++id);
                prepareStatement.setBigDecimal(++i, BigDecimal.valueOf(cargo.getWeight()));
                prepareStatement.setBigDecimal(++i, BigDecimal.valueOf(cargo.getLength()));
                prepareStatement.setBigDecimal(++i, BigDecimal.valueOf(cargo.getWidth()));
                prepareStatement.setBigDecimal(++i, BigDecimal.valueOf(cargo.getHeight()));
                prepareStatement.executeUpdate();

                LOG.info("Successfully create a new cargo in database");
            }
        } catch (SQLException e) {
            LOG.error("Failed to create a new cargo in database", e);
            throw new DBException("Failed to create a new cargo in database", e);
        }
        return findById(id).orElseThrow(DBException::new);
    }

    @Override
    public Optional<Cargo> findById(int cargoId) throws DBException {
        String sql = "SELECT * FROM cargo WHERE id = ?";
        Optional<Cargo> cargo = Optional.empty();
        try (var connection = HikariCP.getHikariConnection()) {
            try (var preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, cargoId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Cargo foundedCargo = build(resultSet);
                    cargo = Optional.of(foundedCargo);
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed to get a cargo by it id from database", e);
            throw new DBException("Failed to get a cargo by it id from database", e);
        }
        LOG.info("Successfully get a cargo by it id from database");
        return cargo;
    }

    @Override
    public void deleteById(int cargoId) throws DBException {
        String sql = "DELETE FROM cargo WHERE id = ?;";
        try (var connection = HikariCP.getHikariConnection()) {
            try (var prepareStatement = connection.prepareStatement(sql)) {
                prepareStatement.setInt(1, cargoId);
                prepareStatement.executeUpdate();
                LOG.info("Successfully delete a cargo from database");
            }
        } catch (SQLException e) {
            LOG.error("Failed to delete a cargo from database", e);
            throw new DBException("Failed to delete a cargo from database", e);
        }
    }

    @Override
    public boolean isExist(int id) throws DBException {
        String sql = "SELECT EXISTS(SELECT id FROM cargo WHERE id = ?)";
        boolean isExist = Boolean.FALSE;
        try (var connection = HikariCP.getHikariConnection()) {
            try (var preparedStatement = connection.prepareStatement(sql,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                preparedStatement.setInt(1, id);
                var resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    isExist = resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            LOG.error("Failed to check cargo existence", e);
            throw new DBException("Failed to check cargo existence", e);
        }
        LOG.info("Successfully check cargo existence");
        return isExist;
    }

    private Cargo build(ResultSet resultSet) throws SQLException {
        return new Cargo(
                resultSet.getInt("id"),
                resultSet.getBigDecimal("length").doubleValue(),
                resultSet.getBigDecimal("width").doubleValue(),
                resultSet.getBigDecimal("height").doubleValue(),
                resultSet.getBigDecimal("weight").doubleValue()
        );
    }

}
