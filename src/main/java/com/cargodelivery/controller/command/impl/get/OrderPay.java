package com.cargodelivery.controller.command.impl.get;

import com.cargodelivery.controller.command.Command;
import com.cargodelivery.controller.command.CommandList;
import com.cargodelivery.dao.entity.User;
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

public class OrderPay implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(OrderPay.class);
    private final OrderService orderService;

    public OrderPay() {
        orderService = new OrderServiceImpl(new OrderDaoImpl(), new UserDaoImpl());
    }

    /**
     * Process code of one of the command from {@link CommandList}
     *
     * @param req  {@link HttpServletRequest}
     * @param resp {@link HttpServletResponse}
     * @return JSP url
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        try {
            int orderId = AppUtils.parseReqParam(req, "orderId");
            User updatedUser = orderService.payForOrder(orderId, user);
            LOG.info("User={} pay for order={} successfully", user.getLogin(), orderId);
            session.setAttribute("user", updatedUser);
            return CommandList.USER_ORDERS.getCommand().execute(req, resp);
        } catch (OrderServiceException | IllegalArgumentException e) {
            LOG.error("User={} pay for order failed", user.getLogin(), e);
            session.setAttribute("errorMessage", e.getMessage());
            return CommandList.ERROR_PAGE.getCommand().execute(req, resp);
        }
    }
}
