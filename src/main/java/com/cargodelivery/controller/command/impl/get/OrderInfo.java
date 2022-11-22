package com.cargodelivery.controller.command.impl.get;

import com.cargodelivery.controller.command.Command;
import com.cargodelivery.controller.command.CommandList;
import com.cargodelivery.dao.entity.Order;
import com.cargodelivery.exception.OrderServiceException;
import com.cargodelivery.service.AppUtils;
import com.cargodelivery.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderInfo implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(OrderInfo.class);
    private final OrderService orderService;
    private final Command userOrders;

    public OrderInfo(OrderService orderService, Command command) {
        this.orderService = orderService;
        this.userOrders = command;
    }

    /**
     * Read details about order from db by id
     * and display it
     *
     * @param req  {@link HttpServletRequest}
     * @param res {@link HttpServletResponse}
     * @return JSP url
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        try {
            int orderId = AppUtils.parseReqParam(req, "orderId");
            Order order = orderService.findOrder(orderId);
            LOG.info("Successfully find info about order{}", order);
            session.setAttribute("orderInfo", order);
            return userOrders.execute(req, res);
        } catch (OrderServiceException | IllegalArgumentException e) {
            LOG.error(e.getMessage(), e);
            session.setAttribute("errorMessage", e.getMessage());
            return CommandList.ERROR_PAGE.getCommand().execute(req, res);
        }
    }
}
