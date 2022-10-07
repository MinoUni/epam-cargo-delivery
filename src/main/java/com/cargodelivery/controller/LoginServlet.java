package com.cargodelivery.controller;

import com.cargodelivery.dao.entity.User;
import com.cargodelivery.exception.UserServiceException;
import com.cargodelivery.service.UserService;
import com.cargodelivery.service.impl.UserServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final String INDEX_PAGE = "index.jsp";
    private static final String LOGIN_PAGE = "login.jsp";
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            User userToLogin = new User(
                    userService.checkRequestParam(req, "login"),
                    userService.checkRequestParam(req, "password")
            );
            User loggedUser = userService.login(userToLogin);
            HttpSession session = req.getSession();
            session.setAttribute("user", loggedUser);
            resp.sendRedirect(INDEX_PAGE);
        } catch (UserServiceException e) {
            resp.sendRedirect(LOGIN_PAGE);
        }
    }
}
