package com.cargodelivery.controller.command.impl.post;

import com.cargodelivery.controller.command.Command;
import com.cargodelivery.dao.entity.User;
import com.cargodelivery.dao.impl.UserDaoImpl;
import com.cargodelivery.exception.UserServiceException;
import com.cargodelivery.service.UserService;
import com.cargodelivery.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.cargodelivery.service.AppUtils.checkReqParam;

public class Login implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(Login.class);
    private static final String INDEX_PAGE = "index.jsp";
    private static final String ERROR_PAGE = "error.jsp";
    private final UserService userService;

    public Login() {
        userService = new UserServiceImpl(new UserDaoImpl());
    }

    /**
     * @param req  {@link HttpServletRequest}
     * @param resp {@link HttpServletResponse}
     * @return JSP name url
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
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
            return ERROR_PAGE;
        }
    }
}
