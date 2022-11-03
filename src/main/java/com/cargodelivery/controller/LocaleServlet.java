package com.cargodelivery.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.cargodelivery.service.AppUtils.checkRequestParam;
import static com.cargodelivery.service.AppUtils.redirectToPage;

@WebServlet("/locale")
public class LocaleServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(LocaleServlet.class);
    private static final String INDEX_PAGE = "index.jsp";
    private static final String ERROR_PAGE = "error.jsp";
    private static final String USER_PROFILE_PAGE = "profile_user.jsp";
    private static final String ADMIN_PROFILE_PAGE = "profile_admin.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        try {
            String page = checkRequestParam(req, "p");
            String locale = checkRequestParam(req, "locale");
            session.setAttribute("locale", locale);
            switch (page) {
                case "index" ->
                        resp.sendRedirect(INDEX_PAGE);
                case "aprofile" ->
                        resp.sendRedirect(ADMIN_PROFILE_PAGE);
                case "uprofile" ->
                        resp.sendRedirect(USER_PROFILE_PAGE);
                default -> {
                    LOG.error("Invalid page={}", page);
                    resp.sendRedirect(ERROR_PAGE);
                }
            }
        } catch (IllegalArgumentException | IOException e) {
            redirectToPage(ERROR_PAGE, session, "errorMessage", e.getMessage(), resp);
        }
    }
}
