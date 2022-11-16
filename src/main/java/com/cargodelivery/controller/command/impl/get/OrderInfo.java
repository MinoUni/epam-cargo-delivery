package com.cargodelivery.controller.command.impl.get;

import com.cargodelivery.controller.command.Command;
import com.cargodelivery.controller.command.CommandList;
import com.cargodelivery.dao.entity.Order;
import com.cargodelivery.dao.impl.OrderDaoImpl;
import com.cargodelivery.dao.impl.UserDaoImpl;
import com.cargodelivery.exception.OrderServiceException;
import com.cargodelivery.service.AppUtils;
import com.cargodelivery.service.OrderService;
import com.cargodelivery.service.impl.OrderServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderInfo implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(OrderInfo.class);
    private final OrderService orderService;

    public OrderInfo() {
        orderService = new OrderServiceImpl(new OrderDaoImpl(), new UserDaoImpl());
    }

    /**
     * @param req  {@link HttpServletRequest}
     * @param resp {@link HttpServletResponse}
     * @return JSP url
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        try {
            int orderId = AppUtils.parseReqParam(req, "orderId");
            Order order = orderService.findOrder(orderId);
            LOG.info("Successfully find info about order{}", order);
            session.setAttribute("orderInfo", order);
            return CommandList.USER_ORDERS.getCommand().execute(req, resp);
        } catch (OrderServiceException | IllegalArgumentException e) {
            LOG.error(e.getMessage(), e);
            session.setAttribute("errorMessage", e.getMessage());
            return CommandList.ERROR_PAGE.getCommand().execute(req, resp);
        }
    }
}
