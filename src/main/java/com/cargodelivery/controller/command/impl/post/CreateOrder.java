package com.cargodelivery.controller.command.impl.post;

import com.cargodelivery.controller.command.Command;
import com.cargodelivery.controller.command.CommandList;
import com.cargodelivery.dao.entity.Cargo;
import com.cargodelivery.dao.entity.Order;
import com.cargodelivery.dao.entity.User;
import com.cargodelivery.dao.entity.enums.OrderState;
import com.cargodelivery.exception.OrderServiceException;
import com.cargodelivery.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.cargodelivery.service.AppUtils.checkReqParam;

public class CreateOrder implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(CreateOrder.class);
    private static final String INDEX_PAGE = "index.jsp";
    private final OrderService orderService;

    public CreateOrder(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Validate data from http request
     * Build it into DTO{@link Order}
     * Save order into database
     *
     * @param req  {@link HttpServletRequest}
     * @param res {@link HttpServletResponse}
     * @return JSP(view) result of the command to ui
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        try {
            Order order = new Order(
                    user.getId(),
                    String.format("%s-%s", checkReqParam(req, "routeStart"), checkReqParam(req, "routeEnd")),
                    new Cargo(
                            Double.parseDouble(checkReqParam(req, "length")),
                            Double.parseDouble(checkReqParam(req, "width")),
                            Double.parseDouble(checkReqParam(req, "height")),
                            Double.parseDouble(checkReqParam(req, "weight"))
                    ),
                    new SimpleDateFormat("yyyy-MM-dd")
                            .parse(LocalDate.now()
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))),
                    OrderState.REGISTERED,
                    BigDecimal.valueOf(Double.parseDouble(checkReqParam(req, "price")))
            );
            orderService.saveOrder(order);
            LOG.info("Order created successfully, order{}", order);
            session.setAttribute("orderMessage", "* Successfully add order in your profile.");
            return INDEX_PAGE;
        } catch (OrderServiceException | IllegalArgumentException | ParseException e) {
            session.setAttribute("errorMessage", e.getMessage());
            return CommandList.ERROR_PAGE.getCommand().execute(req, res);
        }
    }

}
