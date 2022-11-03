package com.cargodelivery.controller;

import com.cargodelivery.dao.entity.Cargo;
import com.cargodelivery.dao.entity.Order;
import com.cargodelivery.dao.entity.User;
import com.cargodelivery.dao.impl.CargoDaoImpl;
import com.cargodelivery.dao.impl.OrderDaoImpl;
import com.cargodelivery.dao.impl.UserDaoImpl;
import com.cargodelivery.exception.OrderServiceException;
import com.cargodelivery.service.OrderService;
import com.cargodelivery.service.impl.OrderServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static com.cargodelivery.service.AppUtils.checkRequestParam;
import static com.cargodelivery.service.AppUtils.redirectToPage;

@WebServlet("/order")
public class OrderServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(OrderServlet.class);
    private static final String PROFILE_PAGE = "profileOrders";
    private static final String ERROR_PAGE = "error.jsp";
    private final OrderService orderService;

    public OrderServlet() {
        this.orderService = new OrderServiceImpl(new CargoDaoImpl(), new OrderDaoImpl(), new UserDaoImpl());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        try {
            String action = checkRequestParam(req, "action");
            switch (action) {
                case "info" -> {
                    String cargoId = checkRequestParam(req, "cargoId");

                    Cargo cargo = orderService.findCargo(cargoId);

                    LOG.info("Successfully find cargo by it id cargoId={}", cargoId);
                    redirectToPage(PROFILE_PAGE, session, "cargo", cargo, resp);
                }
                case "delete" -> {
                    String cargoId = checkRequestParam(req, "cargoId");
                    String orderId = checkRequestParam(req, "orderId");

                    orderService.deleteOrder(orderId, cargoId);
                    List<Order> updatedUserOrdersList = orderService.findAllUserOrders(user);

                    LOG.info("Successfully delete user order, orderId={}", orderId);
                    redirectToPage(PROFILE_PAGE, session, "userOrders", updatedUserOrdersList, resp);
                }
                case "pay" -> {
                    String orderId = checkRequestParam(req, "orderId");

                    user = orderService.payForOrder(orderId, user);

                    LOG.info("User={} successfully paid for order, orderId={}", user.getId(), orderId);
                    redirectToPage(PROFILE_PAGE, session, "user", user, resp);
                }
                default -> {
                    LOG.error("Action={} not exist!", action);
                    redirectToPage(ERROR_PAGE, session, "errorMessage", String.format("Action=%s not exist!", action), resp);
                }
            }
        } catch (OrderServiceException | IllegalArgumentException | IOException e) {
            LOG.error(e.getMessage(), e);
            redirectToPage(ERROR_PAGE, session, "errorMessage", e.getMessage(), resp);
        }
    }


}
