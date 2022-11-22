package com.cargodelivery.controller.command.impl.post;

import com.cargodelivery.controller.command.Command;
import com.cargodelivery.controller.command.CommandList;
import com.cargodelivery.dao.entity.User;
import com.cargodelivery.dao.entity.enums.UserRole;
import com.cargodelivery.exception.UserServiceException;
import com.cargodelivery.service.UserService;
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
    private static final String LOGIN_PAGE = "login.jsp";
    private final UserService userService;

    public Signup(UserService userService) {
        this.userService = userService;
    }

    /**
     * Validate input data from http request.
     * If all okay, then build user data into DTO{@link User} and try
     * to save it into database.
     * If succeeded redirect to login page,
     * otherwise throw exception and redirect to error page
     *
     * @param req  {@link HttpServletRequest}
     * @param res {@link HttpServletResponse}
     * @return JSP(view) result of the command to ui
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        try {
            String password = req.getParameter("password");
            String passwordConfirm = req.getParameter("confirm-password");
            if (!password.equals(passwordConfirm)) {
                LOG.warn("paramName=password({}) and paramName=confirm-password({}) didn't matches", password, passwordConfirm);
                throw new IllegalArgumentException("paramName=password and paramName=confirm-password didn't matches");
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
            return CommandList.ERROR_PAGE.getCommand().execute(req, res);
        }
    }
}
