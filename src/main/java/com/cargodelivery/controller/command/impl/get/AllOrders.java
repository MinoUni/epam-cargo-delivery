package com.cargodelivery.controller.command.impl.get;

import com.cargodelivery.controller.command.Command;
import com.cargodelivery.controller.command.CommandList;
import com.cargodelivery.dao.entity.Order;
import com.cargodelivery.dao.entity.enums.AdminAction;
import com.cargodelivery.exception.OrderServiceException;
import com.cargodelivery.service.AppUtils;
import com.cargodelivery.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AllOrders implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(AllOrders.class);
    private static final String PROFILE_ADMIN_PAGE = "profile_admin.jsp";
    private final OrderService orderService;

    public AllOrders(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * One of the {@link AdminAction} commands
     * Process code of one of the command from {@link CommandList}
     *
     * @param req  {@link HttpServletRequest}
     * @param res {@link HttpServletResponse}
     * @return JSP url
     */
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        try {
            int page = AppUtils.parseReqParam(req, "currentPage");
            List<Order> orders = orderService.getOrdersLimit(page);
            int numbOfPages = orderService.getNumbOfPages();
            LOG.info("Successfully get list of all orders");
            session.setAttribute("adminList", orders);
            session.setAttribute("numbOfPages", numbOfPages);
            session.setAttribute("currentPage", page);
            session.setAttribute("adminAction", AdminAction.ORDERS);
            return PROFILE_ADMIN_PAGE;
        } catch (OrderServiceException | IllegalArgumentException e) {
            LOG.error(e.getMessage(), e);
            session.setAttribute("errorMessage", e.getMessage());
            return CommandList.ERROR_PAGE.getCommand().execute(req, res);
        }
    }

}
