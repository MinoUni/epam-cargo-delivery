package com.cargodelivery.dao.entity;

import com.cargodelivery.dao.entity.enums.UserRole;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class User extends Model{

    private String login;
    private String name;
    private String surname;
    private String email;
    private String password;
    private Date registrationDate;
    private UserRole role;
    private BigDecimal balance;

    // Map from http request signup to model;
    public User(String login, String name, String surname, String email, String password, Date registrationDate, UserRole role) {
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate;
        this.role = role;
    }

    // Map from DB entity to model;
    public User(int id, String login, String name, String surname,
                String email, String password, Date registrationDate, UserRole role, BigDecimal balance) {
        setId(id);
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate;
        this.role = role;
        this.balance = balance;
    }

    // Map from http request login to model;
    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return login.equals(user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }

    @Override
    public String toString() {
        return String.format("User{id=%d, login=%s, name=%s, surname=%s, email=%s, registrationDate=%s, role=%s}", getId(), login, name, surname, email, registrationDate, role.toString());
    }
}
