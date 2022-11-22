package com.cargodelivery.controller.command.impl.post;

import com.cargodelivery.dao.entity.Order;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateOrderTest {

    private final static String INDEX_PAGE = "index.jsp";
    private final static String ERROR_PAGE = "error.jsp";
    private final HttpServletRequest req = mock(HttpServletRequest.class);
    private final HttpServletResponse res = mock(HttpServletResponse.class);
    private final HttpSession session = mock(HttpSession.class);
    private final OrderServiceImpl orderService = mock(OrderServiceImpl.class);
    private final CreateOrder createOrderCommand = new CreateOrder(orderService);
    private final User user = new User(1, "login", "name", "surname", "email", "pass", new Date(1000), UserRole.USER, BigDecimal.valueOf(10));

    @Test
    void executeSuccessfullyTest() throws OrderServiceException {
        when(req.getSession()).thenReturn(session);
        when(session.getAttribute(eq("user"))).thenReturn(user);
        when(req.getParameter(eq("routeStart"))).thenReturn("Lviv");
        when(req.getParameter(eq("routeEnd"))).thenReturn("Ternopil");
        when(req.getParameter(eq("length"))).thenReturn("1.22");
        when(req.getParameter(eq("width"))).thenReturn("1.23");
        when(req.getParameter(eq("height"))).thenReturn("1.24");
        when(req.getParameter(eq("weight"))).thenReturn("1.25");
        when(req.getParameter(eq("price"))).thenReturn("10");

        doNothing().when(orderService).saveOrder(any(Order.class));

        var page = assertDoesNotThrow(() -> createOrderCommand.execute(req, res));
        assertEquals(INDEX_PAGE, page);

        verify(req, times(1)).getSession();
        verify(session, times(1)).getAttribute(eq("user"));
        verify(req, times(7)).getParameter(anyString());
        verify(orderService, times(1)).saveOrder(any(Order.class));
        verify(session, times(1)).setAttribute(eq("orderMessage"), anyString());
    }

    @Test
    void executeFailedWithOneBlankReqParam() throws OrderServiceException {
        when(req.getSession()).thenReturn(session);
        when(session.getAttribute(eq("user"))).thenReturn(user);
        when(req.getParameter(eq("routeStart"))).thenReturn("   ");

        var page = assertDoesNotThrow(() -> createOrderCommand.execute(req, res));
        assertEquals(ERROR_PAGE, page);

        verify(req, times(1)).getSession();
        verify(session, times(1)).getAttribute(eq("user"));
        verify(req, times(1)).getParameter(anyString());
        verify(orderService, never()).saveOrder(any(Order.class));
        verify(session, times(1)).setAttribute(eq("errorMessage"), anyString());
    }
}