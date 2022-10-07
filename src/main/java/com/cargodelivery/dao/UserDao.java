package com.cargodelivery.dao;

import com.cargodelivery.dao.entity.User;
import com.cargodelivery.exception.DBException;

import java.util.Optional;

public interface UserDao {

    void create(User user) throws DBException;

    Optional<User> findById(User user) throws DBException;

    boolean isExist(User user) throws DBException;

    String getPasswordById(User user) throws DBException;
}
