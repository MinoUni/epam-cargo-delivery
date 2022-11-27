package com.cargodelivery.controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandFactoryTest {

    private final HttpServletRequest req = mock(HttpServletRequest.class);
    private final HttpSession session = mock(HttpSession.class);
    private final String paramName = "command";
    private final String attrError = "errorMessage";

    @Test
    void getCommandAcquireCorrectCommandTest() {
        when(req.getParameter(eq(paramName))).thenReturn("LOGOUT");

        var command = assertDoesNotThrow(() -> CommandFactory.getCommand(req));
        assertEquals(CommandList.LOGOUT.getCommand(), command);

        verify(req, times(1)).getParameter(eq(paramName));
        verify(req, never()).getSession();
        verify(session, never()).setAttribute(eq(attrError), anyString());
    }

    @Test
    void getCommandAcquireInvalidCommandTest() {
        when(req.getParameter(eq(paramName))).thenReturn("INVALID_PAGE");
        when(req.getSession()).thenReturn(session);

        var command = assertDoesNotThrow(() -> CommandFactory.getCommand(req));
        assertEquals(CommandList.ERROR_PAGE.getCommand(), command);

        verify(req, times(1)).getParameter(eq(paramName));
        verify(req, times(1)).getSession();
        verify(session, times(1)).setAttribute(eq(attrError), anyString());
    }
}