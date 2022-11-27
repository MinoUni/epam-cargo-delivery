package com.cargodelivery.controller.command.impl.get;

import com.cargodelivery.dao.entity.User;
import com.cargodelivery.dao.entity.enums.UserRole;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogoutTest {
    private static final String INDEX_PAGE = "index.jsp";
    private final HttpServletRequest req = mock(HttpServletRequest.class);
    private final HttpServletResponse res = mock(HttpServletResponse.class);
    private final HttpSession session = mock(HttpSession.class);
    private final Logout logoutCommand = new Logout();
    private final User user = new User(1, "login", "name", "surname", "email", "pass", new Date(1000), UserRole.USER, BigDecimal.valueOf(10));

    @Test
    void executeCommandSuccessfullyTest() {
        String attrUserName = "user";

        when(req.getSession()).thenReturn(session);
        when(session.getAttribute(eq(attrUserName))).thenReturn(user);
        doNothing().when(session).removeAttribute(eq(attrUserName));

        var page = assertDoesNotThrow(() -> logoutCommand.execute(req, res));
        assertEquals(INDEX_PAGE, page);

        verify(req, times(1)).getSession();
        verify(session, times(1)).getAttribute(eq(attrUserName));
        verify(session, times(1)).removeAttribute(eq(attrUserName));
    }
}