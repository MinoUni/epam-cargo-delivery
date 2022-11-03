package com.cargodelivery.controller;

import com.cargodelivery.dao.UserDao;
import com.cargodelivery.dao.entity.AdminAction;
import com.cargodelivery.dao.entity.Order;
import com.cargodelivery.dao.entity.OrderState;
import com.cargodelivery.dao.entity.User;
import com.cargodelivery.dao.impl.CargoDaoImpl;
import com.cargodelivery.dao.impl.OrderDaoImpl;
import com.cargodelivery.dao.impl.UserDaoImpl;
import com.cargodelivery.exception.OrderServiceException;
import com.cargodelivery.exception.UserServiceException;
import com.cargodelivery.service.OrderService;
import com.cargodelivery.service.UserService;
import com.cargodelivery.service.impl.OrderServiceImpl;
import com.cargodelivery.service.impl.UserServiceImpl;
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

@WebServlet("/admin")
public class AdminActionServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(AdminActionServlet.class);
    private static final String PROFILE_PAGE = "profile_admin.jsp";
    private static final String ERROR_PAGE = "error.jsp";
    private static final String PROFILE_LIST_UPDATE = "admin?action=getOrders";
    private final OrderService orderService;
    private final UserService userService;

    public AdminActionServlet() {
        UserDao userRepo = new UserDaoImpl();
        this.userService = new UserServiceImpl(userRepo);
        this.orderService = new OrderServiceImpl(new CargoDaoImpl(), new OrderDaoImpl(), userRepo);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        try {
            String action = checkRequestParam(req, "action");
            switch (action) {
                case "getUsers" -> {
                    List<User> users = userService.findAllUsers();
                    LOG.info("Successfully get list of all users");
                    session.setAttribute("adminList", users);
                    redirectToPage(PROFILE_PAGE, session, "adminAction", AdminAction.USERS, resp);
                }
                case "getOrders" -> {
//                    List<Order> orders = orderService.findAllOrders();
                    String page = checkRequestParam(req, "currentPage");
                    List<Order> orders = orderService.getOrdersLimit(page);
                    int numbOfPages = orderService.getNumbOfPages();
                    LOG.info("Successfully get list of all orders");
                    session.setAttribute("adminList", orders);
                    session.setAttribute("numbOfPages", numbOfPages);
                    session.setAttribute("currentPage", page);
                    redirectToPage(PROFILE_PAGE, session, "adminAction", AdminAction.ORDERS, resp);
                }
                case "approve" -> {
                    String orderId = checkRequestParam(req, "orderId");
                    orderService.updateOrderState(orderId, OrderState.WAITING_FOR_PAYMENT);
                    LOG.info(String.format("Successfully approve order=[%s]", orderId));
                    resp.sendRedirect(PROFILE_LIST_UPDATE);
                }
                case "block" -> {
                    String orderId = checkRequestParam(req, "orderId");
                    orderService.updateOrderState(orderId, OrderState.DECLINED);
                    LOG.info(String.format("Successfully block order=[%s]", orderId));
                    resp.sendRedirect(PROFILE_LIST_UPDATE);
                }
                default -> {
                    LOG.error(String.format("Action [%s] not exist!", action));
                    redirectToPage(ERROR_PAGE, session, "errorMessage", String.format("Action [%s] not exist!", action), resp);
                }
            }
        } catch (UserServiceException | OrderServiceException | IOException e) {
            LOG.error(e.getMessage(), e);
            redirectToPage(ERROR_PAGE, session, "errorMessage", e.getMessage(), resp);
        }
    }
}
