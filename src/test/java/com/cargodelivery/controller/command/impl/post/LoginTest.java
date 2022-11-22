package com.cargodelivery.controller.command.impl.post;

import com.cargodelivery.dao.entity.User;
import com.cargodelivery.exception.UserServiceException;
import com.cargodelivery.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class LoginTest {

    private final static String INDEX_PAGE = "index.jsp";
    private final static String ERROR_PAGE = "error.jsp";
    private final HttpServletRequest req = mock(HttpServletRequest.class);
    private final HttpServletResponse res = mock(HttpServletResponse.class);
    private final HttpSession session = mock(HttpSession.class);
    private final UserServiceImpl userService = mock(UserServiceImpl.class);
    private final Login login = new Login(userService);

    @Test
    void executeSuccessfullyTest() throws UserServiceException {
        User user = new User("test-log", "test-pass");

        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq("login"))).thenReturn("login");
        when(req.getParameter(eq("password"))).thenReturn("pass");

        when(userService.login(any(User.class))).thenReturn(user);

        var page = assertDoesNotThrow(() -> login.execute(req, res));
        assertEquals(INDEX_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(2)).getParameter(anyString());
        verify(userService, times(1)).login(any(User.class));
        verify(session, times(1)).setAttribute(eq("user"), eq(user));
    }

    @Test
    void executeFailedWithOneBlankRequestParamTest() throws UserServiceException {
        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq("login"))).thenReturn("login");
        when(req.getParameter(eq("password"))).thenReturn("      ");

        var page = assertDoesNotThrow(() -> login.execute(req, res));
        assertEquals(ERROR_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(2)).getParameter(anyString());
        verify(userService, never()).login(any(User.class));
        verify(session, times(1)).setAttribute(eq("errorMessage"), anyString());
    }

    @Test
    void executeFailedWithUserServiceExceptionTest() throws UserServiceException {
        var e = new UserServiceException("[TEST]: Service(Model) problems");

        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq("login"))).thenReturn("login");
        when(req.getParameter(eq("password"))).thenReturn("pass");

        when(userService.login(any(User.class))).thenThrow(e);

        var page = assertDoesNotThrow(() -> login.execute(req, res));
        assertEquals(ERROR_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(2)).getParameter(anyString());
        verify(userService, times(1)).login(any(User.class));
        verify(session, times(1)).setAttribute(eq("errorMessage"), eq(e.getMessage()));
    }
}