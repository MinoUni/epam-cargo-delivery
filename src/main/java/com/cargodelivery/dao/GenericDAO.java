package com.cargodelivery.dao;

import com.cargodelivery.dao.entity.Model;
import com.cargodelivery.exception.DBException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GenericDAO<T extends Model, V> {

    default int countNumbOfRecords(String table) throws DBException {
        int numbOfRecords = 0;
        String sql = String.format("SELECT COUNT(id) AS 'id' FROM `%s`;", table);
        try (var conn = ConnPool.getConnection()) {
            try (var prepStatement = conn.prepareStatement(sql)) {
                ResultSet resSet = prepStatement.executeQuery();
                while (resSet.next()) {
                    numbOfRecords = resSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            throw new DBException(String.format("Failed to count number of records in %s table", table), e);
        }
        return numbOfRecords;
    }

    /**
     * Save details about new model into database
     *
     * @param model entities that are subclass of {@link Model}
     * @throws DBException any error with SQL
     */
    void save(T model) throws DBException;

    /**
     * Find all adapted for pagination
     *
     * @param start point from where begin count database records
     * @param numbOfRecords number of records that will be received from database
     * @return list of entities that are subclass of {@link Model}
     * @throws DBException any error with SQL
     */
    List<T> findAllBetween(int start, int numbOfRecords) throws DBException;

    /**
     * Find data in database by unique field value
     *
     * @param field param for filter
     * @return entity that is subclass of {@link Model}
     * @throws DBException Sql error
     */
    Optional<T> findByField(V field) throws DBException;

    /**
     * update data in database for model
     *
     * @param model entity that is subclass of {@link Model}
     * @throws DBException Sql error
     */
    void update(T model) throws DBException;

    /**
     * Build data from database into compact model
     *
     * @param resultSet {@link ResultSet}
     * @return entities that are subclass of {@link Model}
     * @throws SQLException any error with SQL
     */
    T mapToModel(ResultSet resultSet) throws SQLException;

    /**
     * Set data from model into sql query
     *
     * @param preparedStatement {@link PreparedStatement}
     * @param model entities that are subclass of {@link Model}
     * @throws SQLException any error with SQL
     */
    void mapFromModel(PreparedStatement preparedStatement, T model) throws SQLException;

}
