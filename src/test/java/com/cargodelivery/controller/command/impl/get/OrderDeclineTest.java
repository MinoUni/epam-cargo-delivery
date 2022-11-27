package com.cargodelivery.controller.command.impl.get;

import com.cargodelivery.dao.entity.enums.OrderState;
import com.cargodelivery.exception.OrderServiceException;
import com.cargodelivery.service.impl.OrderServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderDeclineTest {

    private static final String USER_PROFILE_PAGE = "profile_user.jsp";
    private final static String ERROR_PAGE = "error.jsp";
    private final HttpServletRequest req = mock(HttpServletRequest.class);
    private final HttpServletResponse res = mock(HttpServletResponse.class);
    private final HttpSession session = mock(HttpSession.class);
    private final OrderServiceImpl orderService = mock(OrderServiceImpl.class);
    private final UserOrders userOrdersCommand = mock(UserOrders.class);
    private final OrderDecline orderDeclineCommand = new OrderDecline(orderService, userOrdersCommand);
    private final String attrErrorName = "errorMessage";
    private final String paramName = "orderId";

    @Test
    void executeCommandSuccessfullyTest() throws OrderServiceException {
        String paramValue = "1";

        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq(paramName))).thenReturn(paramValue);
        doNothing().when(orderService).updateState(any(Integer.class), eq(OrderState.DECLINED));
        when(userOrdersCommand.execute(eq(req), eq(res))).thenReturn(USER_PROFILE_PAGE);

        var page = assertDoesNotThrow(() -> orderDeclineCommand.execute(req, res));
        assertEquals(USER_PROFILE_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(1)).getParameter(eq(paramName));
        verify(orderService, times(1)).updateState(any(Integer.class), eq(OrderState.DECLINED));
        verify(userOrdersCommand, times(1)).execute(eq(req), eq(res));
        verify(session, never()).setAttribute(eq(attrErrorName), any());
    }

    @Test
    void executeCommandFailedWhenReceiveBlankReqParamTest() throws OrderServiceException {
        String paramValue = "    ";

        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq(paramName))).thenReturn(paramValue);

        var page = assertDoesNotThrow(() -> orderDeclineCommand.execute(req, res));
        assertEquals(ERROR_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(1)).getParameter(eq(paramName));
        verify(orderService, never()).updateState(any(Integer.class), eq(OrderState.DECLINED));
        verify(userOrdersCommand, never()).execute(eq(req), eq(res));
        verify(session, times(1)).setAttribute(eq(attrErrorName), any());
    }

    @Test
    void executeCommandFailedWhenReceiveReqParamWithInvalidValueTest() throws OrderServiceException {
        String paramValue = "invalid";

        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq(paramName))).thenReturn(paramValue);

        var page = assertDoesNotThrow(() -> orderDeclineCommand.execute(req, res));
        assertEquals(ERROR_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(1)).getParameter(eq(paramName));
        verify(orderService, never()).updateState(any(Integer.class), eq(OrderState.DECLINED));
        verify(userOrdersCommand, never()).execute(eq(req), eq(res));
        verify(session, times(1)).setAttribute(eq(attrErrorName), any());
    }

    @Test
    void executeCommandFailedWhenUpdateMethodTest() throws OrderServiceException {
        String paramValue = "1";
        var e = new OrderServiceException("[TEST]:Failed");

        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq(paramName))).thenReturn(paramValue);
        doThrow(e).when(orderService).updateState(any(Integer.class), eq(OrderState.DECLINED));

        var page = assertDoesNotThrow(() -> orderDeclineCommand.execute(req, res));
        assertEquals(ERROR_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(1)).getParameter(eq(paramName));
        verify(orderService, times(1)).updateState(any(Integer.class), eq(OrderState.DECLINED));
        verify(userOrdersCommand, never()).execute(eq(req), eq(res));
        verify(session, times(1)).setAttribute(eq(attrErrorName), eq(e.getMessage()));
    }
}