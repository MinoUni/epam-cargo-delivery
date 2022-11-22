package com.cargodelivery.controller.command.impl.get;

import com.cargodelivery.controller.command.Command;
import com.cargodelivery.controller.command.CommandList;
import com.cargodelivery.dao.entity.Order;
import com.cargodelivery.dao.entity.User;
import com.cargodelivery.exception.OrderServiceException;
import com.cargodelivery.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserOrders implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(UserOrders.class);
    private static final String USER_PROFILE_PAGE = "profile_user.jsp";
    private final OrderService orderService;

    public UserOrders(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Look for all orders in db that user make
     *
     * @param req  {@link HttpServletRequest}
     * @param resp {@link HttpServletResponse}
     * @return JSP(view) result of the command to ui
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        try {
            List<Order> orders = orderService.findAllUserOrders(user);
            LOG.info("Successfully find orders for user={}", user.getLogin());
            session.setAttribute("userOrders", orders);
            return USER_PROFILE_PAGE;
        } catch (OrderServiceException e) {
            LOG.error(e.getMessage(), e);
            session.setAttribute("errorMessage", e.getMessage());
            return CommandList.ERROR_PAGE.getCommand().execute(req, resp);
        }
    }
}
