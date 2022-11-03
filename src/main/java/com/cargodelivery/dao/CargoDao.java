package com.cargodelivery.dao;

import com.cargodelivery.dao.entity.Cargo;
import com.cargodelivery.exception.DBException;

import java.util.Optional;

public interface CargoDao {

    /**
     * Save details about cargo into
     * database
     *
     * @param cargo {@link Cargo} data-object with
     *              base details
     * @return {@link Cargo} data-object
     * @throws DBException Sql error
     */
    Cargo save(Cargo cargo) throws DBException;

    /**
     * Find cargo in database by cargo id
     * and return as Cargo model data-object
     *
     * @param cargoId int value of cargo id
     * @return {@link Cargo} data-object or empty Optional
     * @throws DBException Sql error
     */
    Optional<Cargo> findById(int cargoId) throws DBException;

    /**
     * Find cargo in database by cargo id
     * and delete it
     *
     * @param cargoId int value of cargo id
     * @throws DBException Sql error
     */
    void deleteById(int cargoId) throws DBException;

    /**
     * Verify that cargo already
     * exist in database
     *
     * @param id int value of cargo id
     * @return Boolean.TRUE when user exists
     * @throws DBException Sql error
     */
    boolean isExist(int id) throws DBException;
}
