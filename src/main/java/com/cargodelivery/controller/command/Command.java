package com.cargodelivery.controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Command {

    /**
     * Process code of one of the command from {@link CommandList}
     *
     * @param req {@link HttpServletRequest}
     * @param resp {@link HttpServletResponse}
     * @return JSP url
     */
    String execute(HttpServletRequest req, HttpServletResponse resp);
}
