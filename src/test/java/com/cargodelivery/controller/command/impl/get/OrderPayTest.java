package com.cargodelivery.controller.command.impl.get;

import com.cargodelivery.dao.entity.User;
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
class OrderPayTest {

    private static final String USER_PROFILE_PAGE = "profile_user.jsp";
    private final static String ERROR_PAGE = "error.jsp";
    private final HttpServletRequest req = mock(HttpServletRequest.class);
    private final HttpServletResponse res = mock(HttpServletResponse.class);
    private final HttpSession session = mock(HttpSession.class);
    private final OrderServiceImpl orderService = mock(OrderServiceImpl.class);
    private final UserOrders userOrdersCommand = mock(UserOrders.class);
    private final OrderPay orderPayCommand = new OrderPay(orderService, userOrdersCommand);
    private final User user = new User(1, "login", "name", "surname", "email", "pass", new Date(1000), UserRole.USER, BigDecimal.valueOf(10));

    @Test
    void executeCommandSuccessfullyTest() throws OrderServiceException {
        when(req.getSession()).thenReturn(session);
        when(session.getAttribute(eq("user"))).thenReturn(user);
        when(req.getParameter(eq("orderId"))).thenReturn("1");
        when(orderService.payForOrder(any(Integer.class), any(User.class))).thenReturn(user);
        doNothing().when(session).setAttribute(eq("user"), any());
        when(userOrdersCommand.execute(any(), any())).thenReturn(USER_PROFILE_PAGE);

        var page = assertDoesNotThrow(() -> orderPayCommand.execute(req, res));
        assertEquals(USER_PROFILE_PAGE, page);

        verify(req, times(1)).getSession();
        verify(session, times(1)).getAttribute(eq("user"));
        verify(req, times(1)).getParameter(eq("orderId"));
        verify(orderService, times(1)).payForOrder(any(Integer.class), any(User.class));
        verify(session, times(1)).setAttribute(eq("user"), any());
    }

    @Test
    void executeCommandFailedWithBlankReqParamTest() throws OrderServiceException {
        when(req.getSession()).thenReturn(session);
        when(session.getAttribute(eq("user"))).thenReturn(user);
        when(req.getParameter(eq("orderId"))).thenReturn("       ");

        var page = assertDoesNotThrow(() -> orderPayCommand.execute(req, res));
        assertEquals(ERROR_PAGE, page);

        verify(req, times(1)).getSession();
        verify(session, times(1)).getAttribute(eq("user"));
        verify(req, times(1)).getParameter(eq("orderId"));
        verify(orderService, never()).payForOrder(any(Integer.class), any(User.class));
        verify(session, times(1)).setAttribute(eq("errorMessage"), any());
    }

    @Test
    void executeCommandFailedWithInvalidOrderIdReqParamTest() throws OrderServiceException {
        when(req.getSession()).thenReturn(session);
        when(session.getAttribute(eq("user"))).thenReturn(user);
        when(req.getParameter(eq("orderId"))).thenReturn("sds");

        var page = assertDoesNotThrow(() -> orderPayCommand.execute(req, res));
        assertEquals(ERROR_PAGE, page);

        verify(req, times(1)).getSession();
        verify(session, times(1)).getAttribute(eq("user"));
        verify(req, times(1)).getParameter(eq("orderId"));
        verify(orderService, never()).payForOrder(any(Integer.class), any(User.class));
        verify(session, times(1)).setAttribute(eq("errorMessage"), any());
    }

    @Test
    void executeCommandFailedWithOrderServiceExceptionTest() throws OrderServiceException {
        var e = new OrderServiceException("[TEST]: Exception");

        when(req.getSession()).thenReturn(session);
        when(session.getAttribute(eq("user"))).thenReturn(user);
        when(req.getParameter(eq("orderId"))).thenReturn("1");
        when(orderService.payForOrder(any(Integer.class), any(User.class))).thenThrow(e);

        var page = assertDoesNotThrow(() -> orderPayCommand.execute(req, res));
        assertEquals(ERROR_PAGE, page);

        verify(req, times(1)).getSession();
        verify(session, times(1)).getAttribute(eq("user"));
        verify(req, times(1)).getParameter(eq("orderId"));
        verify(orderService, times(1)).payForOrder(any(Integer.class), any(User.class));
        verify(session, times(1)).setAttribute(eq("errorMessage"), eq(e.getMessage()));
    }
}