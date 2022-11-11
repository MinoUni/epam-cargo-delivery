package com.cargodelivery.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AppUtils {

    private static final Logger LOG = LoggerFactory.getLogger(AppUtils.class);

    /**
     * Checks parameter for null & blank value by parameter name
     *
     * @param param   parameter name of request
     * @param request Http request from page
     * @return parameter value
     * @throws IllegalArgumentException when parameter is null or blank
     */
    public static String checkReqParam(HttpServletRequest request, String param) throws IllegalArgumentException {
        String result = request.getParameter(param);
        if (result == null || result.isBlank()) {
            LOG.error("Param from request was Blank or NULL, paramName={} paramValue={}", param, result);
            throw new IllegalArgumentException(String.format("Param from request was Blank or NULL, paramName=%s paramValue=%s", param, result));
        }
        return result;
    }

    /**
     * Redirect to provided jsp and set attribute
     * in session
     *
     * @param pageUrl JSP page name as string
     * @param session Http session
     * @param attributeName name of attribute that set in session
     * @param attributeValue value of attribute to set in session
     * @param resp Http response from jsp
     * @throws IOException when parameter is null or blank
     */
    public static void redirectToPage(String pageUrl, HttpSession session, String attributeName,
                                Object attributeValue, HttpServletResponse resp) throws IOException {
        session.setAttribute(attributeName, attributeValue);
        resp.sendRedirect(pageUrl);
    }
}
