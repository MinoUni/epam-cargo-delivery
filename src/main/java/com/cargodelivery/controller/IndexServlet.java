package com.cargodelivery.controller;

import com.cargodelivery.dao.entity.Cargo;
import com.cargodelivery.dao.entity.Order;
import com.cargodelivery.dao.entity.OrderState;
import com.cargodelivery.dao.entity.User;
import com.cargodelivery.dao.impl.CargoDaoImpl;
import com.cargodelivery.dao.impl.OrderDaoImpl;
import com.cargodelivery.dao.impl.UserDaoImpl;
import com.cargodelivery.exception.OrderServiceException;
import com.cargodelivery.service.OrderService;
import com.cargodelivery.service.impl.OrderServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import static com.cargodelivery.service.AppUtils.checkRequestParam;
import static com.cargodelivery.service.AppUtils.redirectToPage;

@WebServlet(urlPatterns = {"/orderForm", "/profileOrders"})
public class IndexServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(IndexServlet.class);

    private static final String INDEX_PAGE = "index.jsp";

    private static final String ERROR_PAGE = "error.jsp";

    private static final String USER_PROFILE_PAGE = "profile_user.jsp";

    private final OrderService orderService;

    public IndexServlet() {
        this.orderService = new OrderServiceImpl(new CargoDaoImpl(), new OrderDaoImpl(), new UserDaoImpl());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        try {
            List<Order> orders = orderService.findAllUserOrders(user);
            LOG.info("Successfully find all orders for user={}", user.getId());
            redirectToPage(USER_PROFILE_PAGE, session, "userOrders", orders, resp);

        } catch (OrderServiceException | IOException e) {
            redirectToPage(ERROR_PAGE, session, "errorMessage", e.getMessage(), resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Order order = createOrderFromRequest(req, resp);
            orderService.saveOrder(order);

            LOG.info("Order created successfully, orderId={}", order.getId());

            req.setAttribute("orderMessage", "* Order created successfully.");
            req.getRequestDispatcher(INDEX_PAGE).forward(req, resp);

        } catch (OrderServiceException | IOException e) {
            redirectToPage(ERROR_PAGE, req.getSession(), "errorMessage", e.getMessage(), resp);
        }
    }

    private Order createOrderFromRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        Cargo cargo = createCargoFromRequest(req, resp);
        Order order = null;
        try {
            order = new Order(
                    user.getId(),
                    cargo.getId(),
                    BigDecimal.valueOf(Double.parseDouble(checkRequestParam(req, "price"))),
                    checkRequestParam(req, "routeStart"),
                    checkRequestParam(req, "routeEnd"),
                    transferToDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))),
                    transferToDate(checkRequestParam(req, "deliveryDate")),
                    OrderState.REGISTERED
            );
        } catch (IllegalArgumentException e) {
            redirectToPage(ERROR_PAGE, session, "errorMessage", e.getMessage(), resp);
        } catch (ParseException e) {
            LOG.error("Failed to parse Date", e);
            redirectToPage(ERROR_PAGE, session, "errorMessage", "Failed to parse Date", resp);
        }
        return order;
    }

    private Cargo createCargoFromRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Cargo cargo = null;
        HttpSession session = req.getSession();
        try {
            cargo = orderService.saveCargo(
                    new Cargo(
                            Float.parseFloat(checkRequestParam(req, "length")),
                            Float.parseFloat(checkRequestParam(req, "width")),
                            Float.parseFloat(checkRequestParam(req, "height")),
                            Float.parseFloat(checkRequestParam(req, "weight"))
                    )
            );
        } catch (NumberFormatException e) {
            LOG.error("Some of provided values didn't contains parsable float", e);
            redirectToPage(ERROR_PAGE, session, "errorMessage", "Some of provided values didn't contains parsable float", resp);
        } catch (IllegalArgumentException | OrderServiceException e) {
            redirectToPage(ERROR_PAGE, session, "errorMessage", e.getMessage(), resp);
        }
        LOG.info("Successfully create a new Cargo={}", cargo);
        return cargo;
    }

    private Date transferToDate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    }

}
