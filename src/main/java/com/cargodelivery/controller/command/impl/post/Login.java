package com.cargodelivery.controller.command.impl.post;

import com.cargodelivery.controller.command.Command;
import com.cargodelivery.controller.command.CommandList;
import com.cargodelivery.dao.entity.User;
import com.cargodelivery.exception.UserServiceException;
import com.cargodelivery.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.cargodelivery.service.AppUtils.checkReqParam;

public class Login implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(Login.class);
    private static final String INDEX_PAGE = "index.jsp";
    private final UserService userService;

    public Login(UserService userService) {
        this.userService = userService;
    }

    /**
     * @param req  {@link HttpServletRequest}
     * @param res {@link HttpServletResponse}
     * @return JSP(view) result of the command to ui
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        try {
            User userToLogin = new User(checkReqParam(req, "login"), checkReqParam(req, "password"));
            User loggedUser = userService.login(userToLogin);
            session.setAttribute("user", loggedUser);
            LOG.info("Successfully login user={}", loggedUser.getLogin());
            return INDEX_PAGE;
        } catch (IllegalArgumentException | UserServiceException e) {
            LOG.error(e.getMessage(), e);
            session.setAttribute("errorMessage", e.getMessage());
            return CommandList.ERROR_PAGE.getCommand().execute(req, res);
        }
    }
}
