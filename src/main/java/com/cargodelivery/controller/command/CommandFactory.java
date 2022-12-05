package com.cargodelivery.controller.command;

import com.cargodelivery.service.AppUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CommandFactory {

    private static final Logger LOG = LoggerFactory.getLogger(CommandFactory.class);

    private CommandFactory() {}

    /**
     * Verify command existence from user http req
     * In case invalid command redirect to error page
     *
     * @param req {@link HttpServletRequest}
     * @return one of the command from {@link CommandList}
     */
    public static Command getCommand(HttpServletRequest req) {
        String commandName = AppUtils.checkReqParam(req, "command");
        try {
            LOG.info(String.format("Acquired correct command=%s", commandName));
            return CommandList.valueOf(commandName).getCommand();
        } catch (IllegalArgumentException e) {
            HttpSession session = req.getSession();
            LOG.error(String.format("Acquired invalid command=%s, redirect to error page", commandName));
            session.setAttribute("errorMessage", String.format("Acquired invalid command=%s, redirect to error page", commandName));
            return CommandList.ERROR_PAGE.getCommand();
        }
    }
}
