package com.cargodelivery.controller;

import com.cargodelivery.controller.command.Command;
import com.cargodelivery.controller.command.CommandFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/controller")
public class FrontController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String forward = handleHttpRequest(req, res);
        req.getRequestDispatcher(forward).forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String redirect = handleHttpRequest(req, res);
        res.sendRedirect(redirect);
    }

    private String handleHttpRequest(HttpServletRequest req, HttpServletResponse res) {
        Command command = CommandFactory.getCommand(req);
        return command.execute(req, res);
    }
}
