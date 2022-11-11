package com.cargodelivery.controller.command.impl.get;

import com.cargodelivery.controller.command.Command;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ErrorPage implements Command {

    private static final String ERROR_PAGE = "error.jsp";

    /**
     * @param req  {@link HttpServletRequest}
     * @param resp {@link HttpServletResponse}
     * @return JSP name
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        return ERROR_PAGE;
    }
}
