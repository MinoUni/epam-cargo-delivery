package com.cargodelivery.dao.entity;

import java.math.BigDecimal;

public class User {

    private final String id;

    private String name;

    private String surname;

    private String email;

    private String password;

    private UserRole userRole;

    private BigDecimal balance;

    public User(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public User(String id, String name, String surname, String email, String password) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.userRole = UserRole.USER;
        this.balance = BigDecimal.valueOf(10000.00);
    }

    public User(String id,
                String name,
                String surname,
                String email,
                String password,
                UserRole userRole,
                BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.balance = balance;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
