package com.cargodelivery.dao;

import com.cargodelivery.dao.entity.Model;
import com.cargodelivery.dao.entity.User;
import com.cargodelivery.exception.DBException;

/*
 * Interface to define special functionality for work with users table
 */
public interface UserRepository extends GenericDAO<User, String> {

    /**
     * Verify that model with that data already exist in database
     *
     * @param model entities that are subclass of {@link Model}
     * @return Boolean.TRUE when model exists
     * @throws DBException any error with SQL
     */
    boolean isExist(User model) throws DBException;
}
