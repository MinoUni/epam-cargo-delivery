package com.cargodelivery.controller.command.impl.post;

import com.cargodelivery.controller.command.Command;
import com.cargodelivery.dao.entity.User;
import com.cargodelivery.dao.entity.enums.UserRole;
import com.cargodelivery.dao.impl.UserDaoImpl;
import com.cargodelivery.exception.UserServiceException;
import com.cargodelivery.service.UserService;
import com.cargodelivery.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.cargodelivery.service.AppUtils.checkReqParam;

public class Signup implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(Signup.class);

    private static final String ERROR_PAGE = "error.jsp";

    private static final String LOGIN_PAGE = "login.jsp";

    private final UserService userService;

    public Signup() {
        userService = new UserServiceImpl(new UserDaoImpl());
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        try {
            if (!req.getParameter("password").equals(req.getParameter("confirm-password"))) {
                LOG.warn("Requests paramName=password and paramName=confirm-password didn't matches");
                throw new IllegalArgumentException("Requests paramName=password and paramName=confirm-password didn't matches");
            }
            User newUser = new User(
                    checkReqParam(req, "login"),
                    checkReqParam(req, "name"),
                    checkReqParam(req, "surname"),
                    checkReqParam(req, "email"),
                    checkReqParam(req, "password"),
                    new SimpleDateFormat("yyyy-MM-dd")
                            .parse(LocalDate.now()
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))),
                    UserRole.USER
            );
            userService.signup(newUser);
            return LOGIN_PAGE;
        } catch (UserServiceException | IllegalArgumentException | ParseException e) {
            session.setAttribute("errorMessage", e.getMessage());
            return ERROR_PAGE;
        }
    }
}
