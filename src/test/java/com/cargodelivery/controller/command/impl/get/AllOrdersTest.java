package com.cargodelivery.controller.command.impl.get;

import com.cargodelivery.exception.OrderServiceException;
import com.cargodelivery.service.impl.OrderServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AllOrdersTest {

    private static final String ADMIN_PROFILE_PAGE = "profile_admin.jsp";
    private final static String ERROR_PAGE = "error.jsp";
    private final HttpServletRequest req = mock(HttpServletRequest.class);
    private final HttpServletResponse res = mock(HttpServletResponse.class);
    private final HttpSession session = mock(HttpSession.class);
    private final OrderServiceImpl orderService = mock(OrderServiceImpl.class);
    private final AllOrders allOrdersCommand = new AllOrders(orderService);
    private final String paramName = "currentPage";
    private final String attrError = "errorMessage";
    private final String attrAdminList = "adminList";
    private final String attrNumbOfPages = "numbOfPages";
    private final String attrCurrentPage = "currentPage";
    private final String attrAdminAction = "adminAction";

    @Test
    void executeCommandSuccessfullyTest() throws OrderServiceException {
        String paramValue = "1";

        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq(paramName))).thenReturn(paramValue);
        when(orderService.getOrdersLimit(any(Integer.class))).thenReturn(List.of());
        when(orderService.getNumbOfPages()).thenReturn(1);
        doNothing().when(session).setAttribute(eq(attrAdminList), any());
        doNothing().when(session).setAttribute(eq(attrNumbOfPages), any());
        doNothing().when(session).setAttribute(eq(attrCurrentPage), any());
        doNothing().when(session).setAttribute(eq(attrAdminAction), any());

        var page = assertDoesNotThrow(() -> allOrdersCommand.execute(req, res));
        assertEquals(ADMIN_PROFILE_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(1)).getParameter(eq(paramName));
        verify(orderService, times(1)).getOrdersLimit(any(Integer.class));
        verify(orderService, times(1)).getNumbOfPages();
        verify(session, times(4)).setAttribute(any(), any());
        verify(session, never()).setAttribute(eq(attrError), any());
    }

    @Test
    void executeCommandFailedWithBlankReqParamTest() throws OrderServiceException {
        String paramValue = "   ";

        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq(paramName))).thenReturn(paramValue);

        var page = assertDoesNotThrow(() -> allOrdersCommand.execute(req, res));
        assertEquals(ERROR_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(1)).getParameter(eq(paramName));
        verify(orderService, never()).getOrdersLimit(any(Integer.class));
        verify(orderService, never()).getNumbOfPages();
        verify(session, times(1)).setAttribute(eq(attrError), any());
    }

    @Test
    void executeCommandFailedWithInvalidReqParamTest() throws OrderServiceException {
        String paramValue = "invalid";

        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq(paramName))).thenReturn(paramValue);

        var page = assertDoesNotThrow(() -> allOrdersCommand.execute(req, res));
        assertEquals(ERROR_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(1)).getParameter(eq(paramName));
        verify(orderService, never()).getOrdersLimit(any(Integer.class));
        verify(orderService, never()).getNumbOfPages();
        verify(session, times(1)).setAttribute(eq(attrError), any());
    }

    @Test
    void executeCommandFailedWithFindAllUsersServiceExceptionTest() throws OrderServiceException {
        String paramValue = "1";
        var e = new OrderServiceException("[TEST]: Exception");

        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq(paramName))).thenReturn(paramValue);
        when(orderService.getOrdersLimit(any(Integer.class))).thenThrow(e);

        var page = assertDoesNotThrow(() -> allOrdersCommand.execute(req, res));
        assertEquals(ERROR_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(1)).getParameter(eq(paramName));
        verify(orderService, times(1)).getOrdersLimit(any(Integer.class));
        verify(orderService, never()).getNumbOfPages();
        verify(session, times(1)).setAttribute(eq(attrError), eq(e.getMessage()));
    }

    @Test
    void executeCommandFailedWithGetNumbOfPagesServiceExceptionTest() throws OrderServiceException {
        String paramValue = "1";
        var e = new OrderServiceException("[TEST]: Exception");

        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq(paramName))).thenReturn(paramValue);
        when(orderService.getOrdersLimit(any(Integer.class))).thenReturn(List.of());
        when(orderService.getNumbOfPages()).thenThrow(e);

        var page = assertDoesNotThrow(() -> allOrdersCommand.execute(req, res));
        assertEquals(ERROR_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(1)).getParameter(eq(paramName));
        verify(orderService, times(1)).getOrdersLimit(any(Integer.class));
        verify(orderService, times(1)).getNumbOfPages();
        verify(session, never()).setAttribute(eq(attrAdminList), any());
        verify(session, never()).setAttribute(eq(attrNumbOfPages), any());
        verify(session, never()).setAttribute(eq(attrCurrentPage), any());
        verify(session, never()).setAttribute(eq(attrAdminAction), any());
        verify(session, times(1)).setAttribute(eq(attrError), eq(e.getMessage()));
    }
}