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

public class OrderBlock implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(OrderBlock.class);
    private final OrderService orderService;
    private final Command userOrders;

    public OrderBlock(OrderService orderService, Command command, OrderService orderService1, Command userOrders) {
        this.orderService = orderService1;
        this.userOrders = userOrders;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        try {
            int orderId = AppUtils.parseReqParam(req, "orderId");
            orderService.updateState(orderId, OrderState.WAITING_FOR_PAYMENT);
            LOG.trace("Update orderState={} for orderId={}", OrderState.WAITING_FOR_PAYMENT, orderId);
            return userOrders.execute(req, res);
        } catch (OrderServiceException | IllegalArgumentException e) {
            LOG.error(e.getMessage(), e);
            session.setAttribute("errorMessage", e.getMessage());
            return CommandList.ERROR_PAGE.getCommand().execute(req, res);
        }
    }
}
