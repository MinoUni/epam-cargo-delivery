package com.cargodelivery.controller.command;

import com.cargodelivery.service.AppUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CommandFactory {

    private static final Logger LOG = LoggerFactory.getLogger(CommandFactory.class);

    private CommandFactory() {}

    public static Command getCommand(HttpServletRequest req) {
        String commandName = AppUtils.checkReqParam(req, "command");
        try {
            LOG.info(String.format("Acquired correct command=%s", commandName));
            return CommandList.valueOf(commandName).getCommand();
        } catch (IllegalArgumentException e) {
            LOG.error(String.format("Acquired invalid command=%s, redirect to error page", commandName));
            req.getSession().setAttribute("errorMessage", String.format("Acquired invalid command=%s, redirect to error page", commandName));
            return CommandList.ERROR_PAGE.getCommand();
        }
    }
}
