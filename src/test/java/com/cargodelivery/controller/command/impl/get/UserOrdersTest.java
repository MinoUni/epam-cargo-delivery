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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserOrdersTest {

    private static final String USER_PROFILE_PAGE = "profile_user.jsp";
    private final static String ERROR_PAGE = "error.jsp";
    private final HttpServletRequest req = mock(HttpServletRequest.class);
    private final HttpServletResponse res = mock(HttpServletResponse.class);
    private final HttpSession session = mock(HttpSession.class);
    private final OrderServiceImpl orderService = mock(OrderServiceImpl.class);
    private final UserOrders userOrdersCommand = new UserOrders(orderService);
    private final User user = new User(1, "login", "name", "surname", "email", "pass", new Date(1000), UserRole.USER, BigDecimal.valueOf(10));

    @Test
    void executeSuccessfullyTest() throws OrderServiceException {
        when(req.getSession()).thenReturn(session);
        when(session.getAttribute(eq("user"))).thenReturn(user);
        doNothing().when(session).setAttribute(eq("userOrders"), any());

        when(orderService.findAllUserOrders(any(User.class))).thenReturn(List.of());

        var page = assertDoesNotThrow(() -> userOrdersCommand.execute(req, res));
        assertEquals(USER_PROFILE_PAGE, page);

        verify(req, times(1)).getSession();
        verify(session, times(1)).getAttribute(eq("user"));
        verify(orderService, times(1)).findAllUserOrders(any(User.class));
        verify(session, times(1)).setAttribute(eq("userOrders"), any());
    }

    @Test
    void executeFailedWithOrderServiceExceptionTest() throws OrderServiceException {
        var e = new OrderServiceException("[TEST]: error message");

        when(req.getSession()).thenReturn(session);
        when(session.getAttribute(eq("user"))).thenReturn(user);
        when(orderService.findAllUserOrders(any(User.class))).thenThrow(e);
        doNothing().when(session).setAttribute(eq("errorMessage"), anyString());

        var page = assertDoesNotThrow(() -> userOrdersCommand.execute(req, res));
        assertEquals(ERROR_PAGE, page);

        verify(req, times(1)).getSession();
        verify(session, times(1)).getAttribute(eq("user"));
        verify(orderService, times(1)).findAllUserOrders(any(User.class));
        verify(session, never()).setAttribute(eq("userOrders"), any());
        verify(session, times(1)).setAttribute(eq("errorMessage"), eq(e.getMessage()));
    }
}