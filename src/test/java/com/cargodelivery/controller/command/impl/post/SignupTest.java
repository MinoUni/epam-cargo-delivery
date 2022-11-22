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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SignupTest {

    private final static String LOGIN_PAGE = "login.jsp";
    private final static String ERROR_PAGE = "error.jsp";
    private final HttpServletRequest req = mock(HttpServletRequest.class);
    private final HttpServletResponse res = mock(HttpServletResponse.class);
    private final HttpSession session = mock(HttpSession.class);
    private final UserServiceImpl userService = mock(UserServiceImpl.class);
    private final Signup signup = new Signup(userService);

    @Test
    void executeSuccessfullyTest() throws UserServiceException {
        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq("password"))).thenReturn("pass");
        when(req.getParameter(eq("confirm-password"))).thenReturn("pass");

        when(req.getParameter(eq("login"))).thenReturn("test-log");
        when(req.getParameter(eq("name"))).thenReturn("test-name");
        when(req.getParameter(eq("surname"))).thenReturn("test-sur");
        when(req.getParameter(eq("email"))).thenReturn("test@test.com");
        when(req.getParameter(eq("password"))).thenReturn("pass");

        doNothing().when(userService).signup(any(User.class));

        var page = assertDoesNotThrow(() -> signup.execute(req, res));
        assertEquals(LOGIN_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(7)).getParameter(anyString());
        verify(userService, times(1)).signup(any(User.class));
    }

    @Test
    void executeFailedWithPasswordMismatchTest() throws UserServiceException {
        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq("password"))).thenReturn("pass");
        when(req.getParameter(eq("confirm-password"))).thenReturn("ssap");

        var page = assertDoesNotThrow(() -> signup.execute(req, res));
        assertEquals(ERROR_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(2)).getParameter(anyString());
        verify(userService, never()).signup(any(User.class));
        verify(session, times(1)).setAttribute(anyString(), anyString());
    }

    @Test
    void executeFailedWithUserServiceExceptionTest() throws UserServiceException {
        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq("password"))).thenReturn("pass");
        when(req.getParameter(eq("confirm-password"))).thenReturn("pass");

        when(req.getParameter(eq("login"))).thenReturn("test-log");
        when(req.getParameter(eq("name"))).thenReturn("test-name");
        when(req.getParameter(eq("surname"))).thenReturn("test-sur");
        when(req.getParameter(eq("email"))).thenReturn("test@test.com");
        when(req.getParameter(eq("password"))).thenReturn("pass");

        doThrow(new UserServiceException("[TEST]:Signup error!")).when(userService).signup(any(User.class));

        var page = assertDoesNotThrow(() -> signup.execute(req, res));
        assertEquals(ERROR_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(7)).getParameter(anyString());
        verify(userService, times(1)).signup(any(User.class));
        verify(session, times(1)).setAttribute(anyString(), anyString());
    }

    @Test
    void executeFailedWithOneParamBlankTest() throws UserServiceException {
        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq("password"))).thenReturn("pass");
        when(req.getParameter(eq("confirm-password"))).thenReturn("pass");
        when(req.getParameter(eq("login"))).thenReturn("   ");

        var page = assertDoesNotThrow(() -> signup.execute(req, res));
        assertEquals(ERROR_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(3)).getParameter(anyString());
        verify(userService, never()).signup(any(User.class));
        verify(session, times(1)).setAttribute(anyString(), anyString());
    }
}