package com.cargodelivery.controller.command.impl.get;

import com.cargodelivery.controller.command.Command;
import com.cargodelivery.dao.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logout implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(Logout.class);
    private static final String INDEX_PAGE = "index.jsp";

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        var user = (User) session.getAttribute("user");
        session.removeAttribute("user");
        LOG.info("Successfully logout user={}", user.getLogin());
        return INDEX_PAGE;
    }
}
