package com.cargodelivery.controller;

import com.cargodelivery.dao.entity.User;
import com.cargodelivery.dao.impl.UserDaoImpl;
import com.cargodelivery.exception.UserServiceException;
import com.cargodelivery.service.UserService;
import com.cargodelivery.service.impl.UserServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.cargodelivery.service.AppUtils.*;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(SignupServlet.class);

    private static final String ERROR_PAGE = "error.jsp";

    private static final String LOGIN_PAGE = "login.jsp";

    private final UserService userService;

    public SignupServlet() {
        this.userService = new UserServiceImpl(new UserDaoImpl());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        try {
            if (!req.getParameter("password").equals(req.getParameter("confirm-password"))) {
                LOG.warn("Requests paramName=password and paramName=confirm-password didn't matches");
                throw new IllegalArgumentException("Requests paramName=password and paramName=confirm-password didn't matches");
            }
            User newUser = new User(
                    checkRequestParam(req, "login"),
                    checkRequestParam(req, "name"),
                    checkRequestParam(req, "surname"),
                    checkRequestParam(req, "email"),
                    checkRequestParam(req, "password")
            );
            userService.signup(newUser);
            resp.sendRedirect(LOGIN_PAGE);
        } catch (UserServiceException | IllegalArgumentException e) {
            redirectToPage(ERROR_PAGE, session, "errorMessage", e.getMessage(), resp);
        }
    }
}
