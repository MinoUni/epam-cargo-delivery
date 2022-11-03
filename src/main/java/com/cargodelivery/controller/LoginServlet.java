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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(LoginServlet.class);
    private static final String INDEX_PAGE = "index.jsp";
    private static final String ERROR_PAGE = "error.jsp";
    private final UserService userService;

    public LoginServlet() {
        this.userService = new UserServiceImpl(new UserDaoImpl());
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        var user = (User) session.getAttribute("user");
        session.removeAttribute("user");
        LOG.info("Successfully logout user={}", user.getId());
        resp.sendRedirect(INDEX_PAGE);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        try {
            User userToLogin = new User(checkRequestParam(req, "login"), checkRequestParam(req, "password"));
            User loggedUser = userService.login(userToLogin);
            LOG.info("Successfully login user={}", loggedUser.getId());
            redirectToPage(INDEX_PAGE, session, "user", loggedUser, resp);
        } catch (IllegalArgumentException | UserServiceException e) {
            redirectToPage(ERROR_PAGE, session, "errorMessage", e.getMessage(), resp);
        }
    }

}
