package com.cargodelivery.controller.command.impl.get;

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

public class AddBalance implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(AddBalance.class);
    private static final String PROFILE_PAGE = "profile_user.jsp";
    private final UserService userService;

    public AddBalance(UserService userService) {
        this.userService = userService;
    }
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        try {
            userService.addBalance(user);
            user = userService.findUser(user);
            LOG.info("Update balance for user={}", user.getLogin());
            session.setAttribute("user", user);
            return PROFILE_PAGE;
        } catch (UserServiceException e) {
            LOG.error(e.getMessage(), e);
            session.setAttribute("errorMessage", e.getMessage());
            return CommandList.ERROR_PAGE.getCommand().execute(req, res);
        }
    }
}
