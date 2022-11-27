package com.cargodelivery.controller.command.impl.get;

import com.cargodelivery.dao.entity.User;
import com.cargodelivery.dao.entity.enums.UserRole;
import com.cargodelivery.exception.OrderServiceException;
import com.cargodelivery.exception.UserServiceException;
import com.cargodelivery.service.impl.OrderServiceImpl;
import com.cargodelivery.service.impl.UserServiceImpl;
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
class OrderBillTest {

    private final static String ERROR_PAGE = "error.jsp";
    private final HttpServletRequest req = mock(HttpServletRequest.class);
    private final HttpServletResponse res = mock(HttpServletResponse.class);
    private final HttpSession session = mock(HttpSession.class);
    private final OrderServiceImpl orderService = mock(OrderServiceImpl.class);
    private final UserServiceImpl userService = mock(UserServiceImpl.class);
    private final OrderBill orderBillCommand = new OrderBill(orderService, userService);
    private final User user = new User(1, "login", "name", "surname", "email", "pass", new Date(1000), UserRole.USER, BigDecimal.valueOf(10));
    private final String attrErrorName = "errorMessage";
    private final String attrUserName = "user";
    private final String paramName = "orderId";

    @Test
    void executeCommandFailedWithBlankReqParamTest() {
        String paramValue = "    ";

        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq(paramName))).thenReturn(paramValue);
        when(session.getAttribute(eq(attrUserName))).thenReturn(user);

        var page = assertDoesNotThrow(() -> orderBillCommand.execute(req, res));
        assertEquals(ERROR_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(1)).getParameter(eq(paramName));
        verify(req, never()).getAttribute(eq(attrUserName));
        verify(session, times(1)).setAttribute(eq(attrErrorName), anyString());
    }

    @Test
    void executeCommandFailedWithInvalidOrderIdTest() {
        String paramValue = "invalid";

        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq(paramName))).thenReturn(paramValue);
        when(session.getAttribute(eq(attrUserName))).thenReturn(user);

        var page = assertDoesNotThrow(() -> orderBillCommand.execute(req, res));
        assertEquals(ERROR_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(1)).getParameter(eq(paramName));
        verify(req, never()).getAttribute(eq(attrUserName));
        verify(session, times(1)).setAttribute(eq(attrErrorName), anyString());
    }

    @Test
    void executeCommandFailedWithUserServiceExceptionTest() throws UserServiceException, OrderServiceException {
        String paramValue = "1";
        var e = new UserServiceException("[TEST]: Exception");

        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq(paramName))).thenReturn(paramValue);
        when(session.getAttribute(eq(attrUserName))).thenReturn(user);
        when(userService.findUser(any(User.class))).thenThrow(e);

        var page = assertDoesNotThrow(() -> orderBillCommand.execute(req, res));
        assertEquals(ERROR_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(1)).getParameter(eq(paramName));
        verify(req, never()).getAttribute(eq(attrUserName));
        verify(userService, times(1)).findUser(any(User.class));
        verify(orderService, never()).findOrder(any(Integer.class));
        verify(session, times(1)).setAttribute(eq(attrErrorName), eq(e.getMessage()));
    }

    @Test
    void executeCommandFailedWithOrderServiceExceptionTest() throws UserServiceException, OrderServiceException {
        String paramValue = "1";
        var e = new OrderServiceException("[TEST]: Exception");

        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq(paramName))).thenReturn(paramValue);
        when(session.getAttribute(eq(attrUserName))).thenReturn(user);
        when(userService.findUser(any(User.class))).thenReturn(user);
        when(orderService.findOrder(any(Integer.class))).thenThrow(e);

        var page = assertDoesNotThrow(() -> orderBillCommand.execute(req, res));
        assertEquals(ERROR_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(1)).getParameter(eq(paramName));
        verify(req, never()).getAttribute(eq(attrUserName));
        verify(userService, times(1)).findUser(any(User.class));
        verify(orderService, times(1)).findOrder(any(Integer.class));
        verify(session, times(1)).setAttribute(eq(attrErrorName), eq(e.getMessage()));
    }
}