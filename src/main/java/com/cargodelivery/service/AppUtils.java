package com.cargodelivery.service;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppUtils {

    private static final Logger LOG = LoggerFactory.getLogger(AppUtils.class);

    /**
     * Checks parameter for null & blank value by parameter name
     *
     * @param paramName   parameter name of request
     * @param request Http request from page
     * @return parameter value
     * @throws IllegalArgumentException when parameter is null or blank
     */
    public static String checkReqParam(HttpServletRequest request, String paramName) throws IllegalArgumentException {
        String param = request.getParameter(paramName);
        if (param == null || param.isBlank()) {
            LOG.error("Param from request was Blank or NULL, paramName={} paramValue={}", paramName, param);
            throw new IllegalArgumentException(String.format("Param from request was Blank or NULL, paramName=%s paramValue=%s", paramName, param));
        }
        return param;
    }

    /**
     * Parse int-type parameters from http requests
     *
     * @param paramName parameter name of request
     * @param req Http request from page
     * @return parameter value
     * @throws IllegalArgumentException when parameter is null or blank
     */
    public static int parseReqParam(HttpServletRequest req, String paramName) throws IllegalArgumentException {
        try {
            return Integer.parseInt(checkReqParam(req, paramName));
        } catch (NumberFormatException e) {
            LOG.error("Invalid param={} was provided", paramName, e);
            throw new IllegalArgumentException(String.format("Invalid param=%s was provided", paramName));
        } catch (IllegalArgumentException e) {
            LOG.error(e.getMessage(), e);
            throw e;
        }
    }

}
