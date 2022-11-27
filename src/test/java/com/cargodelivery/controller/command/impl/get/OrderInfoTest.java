package com.cargodelivery.controller.command.impl.get;

import com.cargodelivery.dao.entity.Cargo;
import com.cargodelivery.dao.entity.Order;
import com.cargodelivery.dao.entity.User;
import com.cargodelivery.dao.entity.enums.OrderState;
import com.cargodelivery.dao.entity.enums.UserRole;
import com.cargodelivery.exception.OrderServiceException;
import com.cargodelivery.service.impl.OrderServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderInfoTest {

    private static final String USER_PROFILE_PAGE = "profile_user.jsp";
    private final static String ERROR_PAGE = "error.jsp";
    private final HttpServletRequest req = mock(HttpServletRequest.class);
    private final HttpServletResponse res = mock(HttpServletResponse.class);
    private final HttpSession session = mock(HttpSession.class);
    private final OrderServiceImpl orderService = mock(OrderServiceImpl.class);
    private final UserOrders userOrdersCommand = mock(UserOrders.class);
    private final OrderInfo orderInfoCommand = new OrderInfo(orderService, userOrdersCommand);
    private final User user = new User(1, "login", "name", "surname", "email", "pass", new Date(1000), UserRole.USER, BigDecimal.valueOf(10));
    private final Cargo cargo = new Cargo(1, 11.1, 11.1, 11.1);
    private final Order order = new Order(1, user.getId(), "route", cargo, new Date(10000), new Date(30000), OrderState.WAITING_FOR_PAYMENT, BigDecimal.valueOf(10.00));
    private final String attrErrorName = "errorMessage";
    private final String paramName = "orderId";

    @Test
    void executeCommandSuccessfullyTest() throws OrderServiceException {
        String attrName = "orderInfo";
        String paramValue = "1";

        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq(paramName))).thenReturn(paramValue);
        when(orderService.findOrder(any(Integer.class))).thenReturn(order);
        doNothing().when(session).setAttribute(eq(attrName), any());
        when(userOrdersCommand.execute(any(), any())).thenReturn(USER_PROFILE_PAGE);

        var page = assertDoesNotThrow(() -> orderInfoCommand.execute(req, res));
        assertEquals(USER_PROFILE_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(1)).getParameter(eq(paramName));
        verify(orderService, times(1)).findOrder(any(Integer.class));
        verify(session, times(1)).setAttribute(eq(attrName), any());
    }

    @Test
    void executeCommandFailedWithBlankReqParamTest() throws OrderServiceException {
        String paramValue = "            ";

        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq(paramName))).thenReturn(paramValue);

        var page = assertDoesNotThrow(() -> orderInfoCommand.execute(req, res));
        assertEquals(ERROR_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(1)).getParameter(eq(paramName));
        verify(orderService, never()).findOrder(any(Integer.class));
        verify(session, times(1)).setAttribute(eq(attrErrorName), any());
    }

    @Test
    void executeCommandFailedWithInvalidOrderIdReqParamTest() throws OrderServiceException {
        String paramValue = "invalid";

        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq(paramName))).thenReturn(paramValue);

        var page = assertDoesNotThrow(() -> orderInfoCommand.execute(req, res));
        assertEquals(ERROR_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(1)).getParameter(eq(paramName));
        verify(orderService, never()).findOrder(any(Integer.class));
        verify(session, times(1)).setAttribute(eq(attrErrorName), any());
    }

    @Test
    void executeCommandFailedWithOrderServiceExceptionTest() throws OrderServiceException {
        String paramValue = "1";
        var e = new OrderServiceException("[TEST]: Exception");

        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq(paramName))).thenReturn(paramValue);
        when(orderService.findOrder(any(Integer.class))).thenThrow(e);
        doNothing().when(session).setAttribute(eq(attrErrorName), eq(e.getMessage()));

        var page = assertDoesNotThrow(() -> orderInfoCommand.execute(req, res));
        assertEquals(ERROR_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(1)).getParameter(eq(paramName));
        verify(orderService, times(1)).findOrder(any(Integer.class));
        verify(session, times(1)).setAttribute(eq(attrErrorName), eq(e.getMessage()));
    }
}