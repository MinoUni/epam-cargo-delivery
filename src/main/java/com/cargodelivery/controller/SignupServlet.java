package com.cargodelivery.controller;

import com.cargodelivery.dao.entity.User;
import com.cargodelivery.exception.UserServiceException;
import com.cargodelivery.service.UserService;
import com.cargodelivery.service.impl.UserServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {

    private final static Logger logger = LogManager.getLogger();
    private static final String ERROR_PAGE = "error.jsp";

    private static final String LOGIN_PAGE = "login.jsp";

    private static final String SIGNUP_PAGE = "signup.jsp";

    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            if (req.getParameter("password").equals(req.getParameter("confirm-password"))) {
                User newUser = new User(
                        userService.checkRequestParam(req, "login"),
                        userService.checkRequestParam(req, "name"),
                        userService.checkRequestParam(req, "surname"),
                        userService.checkRequestParam(req, "email"),
                        userService.checkRequestParam(req, "password")
                );
                userService.signup(newUser);
                resp.sendRedirect(LOGIN_PAGE);
            } else {
                throw new IllegalArgumentException("Password didn't match");
            }
        } catch (UserServiceException e) {
            logger.log(Level.ERROR, "Failed to signup a new user", e);
            resp.sendRedirect(ERROR_PAGE);
        } catch (IllegalArgumentException e) {
            logger.log(Level.ERROR, "Some of the request param=null or blank", e);
            resp.sendRedirect(SIGNUP_PAGE);
        }
    }
}
