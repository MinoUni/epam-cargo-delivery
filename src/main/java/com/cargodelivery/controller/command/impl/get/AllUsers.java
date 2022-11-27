package com.cargodelivery.controller.command.impl.get;

import com.cargodelivery.controller.command.Command;
import com.cargodelivery.controller.command.CommandList;
import com.cargodelivery.dao.entity.User;
import com.cargodelivery.dao.entity.enums.AdminAction;
import com.cargodelivery.exception.UserServiceException;
import com.cargodelivery.service.AppUtils;
import com.cargodelivery.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AllUsers implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(AllUsers.class);
    private static final String PROFILE_ADMIN_PAGE = "profile_admin.jsp";
    private final UserService userService;

    public AllUsers(UserService userService) {
        this.userService = userService;
    }

    /**
     * One of the {@link AdminAction} commands
     * Process code of one of the command from {@link CommandList}
     *
     * @param req  {@link HttpServletRequest}
     * @param res {@link HttpServletResponse}
     * @return JSP url
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        try {
            int page = AppUtils.parseReqParam(req, "currentPage");
            List<User> users = userService.findAllUsers(page);
            int numbOfPages = userService.getNumbOfPages();
            LOG.info("Successfully get list of all users");
            session.setAttribute("adminList", users);
            session.setAttribute("numbOfPages", numbOfPages);
            session.setAttribute("currentPage", page);
            session.setAttribute("adminAction", AdminAction.USERS);
            return PROFILE_ADMIN_PAGE;
        } catch (UserServiceException | IllegalArgumentException e) {
            LOG.error(e.getMessage(), e);
            session.setAttribute("errorMessage", e.getMessage());
            return CommandList.ERROR_PAGE.getCommand().execute(req, res);
        }
    }


}
