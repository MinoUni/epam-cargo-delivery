package com.cargodelivery.controller.command.impl.get;

import com.cargodelivery.controller.command.Command;
import com.cargodelivery.controller.command.CommandList;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.cargodelivery.service.AppUtils.checkReqParam;

public class Locale implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(Locale.class);
    private static final String INDEX_PAGE = "index.jsp";
    private static final String USER_PROFILE_PAGE = "profile_user.jsp";
    private static final String ADMIN_PROFILE_PAGE = "profile_admin.jsp";

    /**
     * Switch webapp localization
     *
     * @param req  {@link HttpServletRequest}
     * @param res {@link HttpServletResponse}
     * @return JSP url
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        try {
            String page = checkReqParam(req, "page");
            String locale = checkReqParam(req, "locale");
            session.setAttribute("locale", locale);
            switch (page) {
                case "index" -> {
                    return INDEX_PAGE;
                }
                case "adminProfile" -> {
                    return ADMIN_PROFILE_PAGE;
                }
                case "userProfile" -> {
                    return USER_PROFILE_PAGE;
                }
                default -> {
                    LOG.error("Invalid page={}", page);
                    return CommandList.ERROR_PAGE.getCommand().execute(req, res);
                }
            }
        } catch (IllegalArgumentException e) {
            session.setAttribute("errorMessage", e.getMessage());
            LOG.error(e.getMessage(), e);
            return CommandList.ERROR_PAGE.getCommand().execute(req, res);
        }
    }
}
