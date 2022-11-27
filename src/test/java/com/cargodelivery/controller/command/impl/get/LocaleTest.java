package com.cargodelivery.controller.command.impl.get;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocaleTest {

    private static final String INDEX_PAGE = "index.jsp";
    private static final String USER_PROFILE_PAGE = "profile_user.jsp";
    private static final String ADMIN_PROFILE_PAGE = "profile_admin.jsp";
    private final static String ERROR_PAGE = "error.jsp";
    private final HttpServletRequest req = mock(HttpServletRequest.class);
    private final HttpServletResponse res = mock(HttpServletResponse.class);
    private final HttpSession session = mock(HttpSession.class);
    private final Locale localeCommand = new Locale();
    private final String attrErrorName = "errorMessage";
    private final String paramPage = "page";
    private final String paramLocale = "locale"; // {en, uk}

    @Test
    void executeCommandCaseIndexSuccessfullyTest() {
        String paramPageValue = "index";
        String paramLocaleValue = "en";

        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq(paramPage))).thenReturn(paramPageValue);
        when(req.getParameter(eq(paramLocale))).thenReturn(paramLocaleValue);
        doNothing().when(session).setAttribute(eq(paramLocale), eq(paramLocaleValue));

        var page = assertDoesNotThrow(() -> localeCommand.execute(req, res));
        assertEquals(INDEX_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(1)).getParameter(eq(paramPage));
        verify(req, times(1)).getParameter(eq(paramLocale));
        verify(session, times(1)).setAttribute(eq(paramLocale), eq(paramLocaleValue));
        verify(session, never()).setAttribute(eq(attrErrorName), any());
    }

    @Test
    void executeCommandCaseProfileAdminSuccessfullyTest() {
        String paramPageValue = "adminProfile";
        String paramLocaleValue = "en";

        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq(paramPage))).thenReturn(paramPageValue);
        when(req.getParameter(eq(paramLocale))).thenReturn(paramLocaleValue);
        doNothing().when(session).setAttribute(eq(paramLocale), eq(paramLocaleValue));

        var page = assertDoesNotThrow(() -> localeCommand.execute(req, res));
        assertEquals(ADMIN_PROFILE_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(1)).getParameter(eq(paramPage));
        verify(req, times(1)).getParameter(eq(paramLocale));
        verify(session, times(1)).setAttribute(eq(paramLocale), eq(paramLocaleValue));
        verify(session, never()).setAttribute(eq(attrErrorName), any());
    }

    @Test
    void executeCommandCaseProfileUserSuccessfullyTest() {
        String paramPageValue = "userProfile";
        String paramLocaleValue = "en";

        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq(paramPage))).thenReturn(paramPageValue);
        when(req.getParameter(eq(paramLocale))).thenReturn(paramLocaleValue);
        doNothing().when(session).setAttribute(eq(paramLocale), eq(paramLocaleValue));

        var page = assertDoesNotThrow(() -> localeCommand.execute(req, res));
        assertEquals(USER_PROFILE_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(1)).getParameter(eq(paramPage));
        verify(req, times(1)).getParameter(eq(paramLocale));
        verify(session, times(1)).setAttribute(eq(paramLocale), eq(paramLocaleValue));
        verify(session, never()).setAttribute(eq(attrErrorName), any());
    }

    @Test
    void executeCommandCaseDefaultSuccessfullyTest() {
        String paramPageValue = "managerProfile";
        String paramLocaleValue = "uk";

        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq(paramPage))).thenReturn(paramPageValue);
        when(req.getParameter(eq(paramLocale))).thenReturn(paramLocaleValue);
        doNothing().when(session).setAttribute(eq(paramLocale), eq(paramLocaleValue));

        var page = assertDoesNotThrow(() -> localeCommand.execute(req, res));
        assertEquals(ERROR_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(1)).getParameter(eq(paramPage));
        verify(req, times(1)).getParameter(eq(paramLocale));
        verify(session, times(1)).setAttribute(eq(paramLocale), eq(paramLocaleValue));
        verify(session, never()).setAttribute(eq(attrErrorName), any());
    }

    @Test
    void executeCommandFailedWithBlankPageReqParamTest() {
        String paramPageValue = "     ";
        String paramLocaleValue = "uk";

        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq(paramPage))).thenReturn(paramPageValue);

        var page = assertDoesNotThrow(() -> localeCommand.execute(req, res));
        assertEquals(ERROR_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(1)).getParameter(eq(paramPage));
        verify(req, never()).getParameter(eq(paramLocale));
        verify(session, never()).setAttribute(eq(paramLocale), eq(paramLocaleValue));
        verify(session, times(1)).setAttribute(eq(attrErrorName), any());
    }

    @Test
    void executeCommandFailedWithBlankLocaleReqParamTest() {
        String paramPageValue = "index";
        String paramLocaleValue = "        ";

        when(req.getSession()).thenReturn(session);
        when(req.getParameter(eq(paramPage))).thenReturn(paramPageValue);
        when(req.getParameter(eq(paramLocale))).thenReturn(paramLocaleValue);

        var page = assertDoesNotThrow(() -> localeCommand.execute(req, res));
        assertEquals(ERROR_PAGE, page);

        verify(req, times(1)).getSession();
        verify(req, times(1)).getParameter(eq(paramPage));
        verify(req, times(1)).getParameter(eq(paramLocale));
        verify(session, never()).setAttribute(eq(paramLocale), eq(paramLocaleValue));
        verify(session, times(1)).setAttribute(eq(attrErrorName), any());
    }
}