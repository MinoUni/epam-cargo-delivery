package com.cargodelivery.controller.command.impl.get;

import com.cargodelivery.controller.command.Command;
import com.cargodelivery.controller.command.CommandList;
import com.cargodelivery.dao.entity.enums.OrderState;
import com.cargodelivery.exception.OrderServiceException;
import com.cargodelivery.service.AppUtils;
import com.cargodelivery.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderDecline implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(OrderDecline.class);
    private final OrderService orderService;
    private final Command allOrders;

    public OrderDecline(OrderService orderService, Command command) {
        this.orderService = orderService;
        this.allOrders = command;
    }

    /**
     * Manager command to update status for registered user's orders
     * Process code of one of the command from {@link CommandList}
     *
     * @param req  {@link HttpServletRequest}
     * @param res {@link HttpServletResponse}
     * @return JSP url
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        final OrderState state = OrderState.DECLINED;
        try {
            int orderId = AppUtils.parseReqParam(req, "orderId");
            orderService.updateState(orderId, state);
            LOG.trace("Update orderState={} for orderId={}", state, orderId);
            return allOrders.execute(req, res);
        } catch (OrderServiceException | IllegalArgumentException e) {
            LOG.error(e.getMessage(), e);
            session.setAttribute("errorMessage", e.getMessage());
            return CommandList.ERROR_PAGE.getCommand().execute(req, res);
        }
    }
}
