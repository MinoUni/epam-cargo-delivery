package com.cargodelivery.controller.command;

import com.cargodelivery.controller.command.impl.get.*;
import com.cargodelivery.controller.command.impl.post.CreateOrder;
import com.cargodelivery.controller.command.impl.post.Login;
import com.cargodelivery.controller.command.impl.post.Signup;
import com.cargodelivery.dao.impl.OrderDaoImpl;
import com.cargodelivery.dao.impl.UserDaoImpl;
import com.cargodelivery.service.impl.OrderServiceImpl;
import com.cargodelivery.service.impl.UserServiceImpl;

public enum CommandList {

    ERROR_PAGE(new ErrorPage()),
    SIGNUP(new Signup(new UserServiceImpl(new UserDaoImpl()))),
    LOGIN(new Login(new UserServiceImpl(new UserDaoImpl()))),
    LOGOUT(new Logout()),
    LOCALE(new Locale()),

    CREATE_ORDER(new CreateOrder(new OrderServiceImpl(new OrderDaoImpl(), new UserDaoImpl()))),

    USER_ORDERS(new UserOrders(new OrderServiceImpl(new OrderDaoImpl(), new UserDaoImpl()))),

    ORDER_INFO(new OrderInfo(new OrderServiceImpl(new OrderDaoImpl(), new UserDaoImpl()),
            new UserOrders(new OrderServiceImpl(new OrderDaoImpl(), new UserDaoImpl())))),

    ORDER_DELETE(new OrderDelete(new OrderServiceImpl(new OrderDaoImpl(), new UserDaoImpl()),
            new UserOrders(new OrderServiceImpl(new OrderDaoImpl(), new UserDaoImpl())))),

    ORDER_PAY(new OrderPay(new OrderServiceImpl(new OrderDaoImpl(), new UserDaoImpl()),
            new UserOrders(new OrderServiceImpl(new OrderDaoImpl(), new UserDaoImpl())))),

    ORDER_BILL(new OrderBill(new OrderServiceImpl(new OrderDaoImpl(), new UserDaoImpl()),
            new UserServiceImpl(new UserDaoImpl()))),

    ORDER_APPROVE(new OrderApprove(new OrderServiceImpl(new OrderDaoImpl(), new UserDaoImpl()),
            new AllOrders(new OrderServiceImpl(new OrderDaoImpl(), new UserDaoImpl())))),

    ORDER_DECLINE(new OrderDecline(new OrderServiceImpl(new OrderDaoImpl(), new UserDaoImpl()),
            new AllOrders(new OrderServiceImpl(new OrderDaoImpl(), new UserDaoImpl())))),

    GET_ORDERS(new AllOrders(new OrderServiceImpl(new OrderDaoImpl(), new UserDaoImpl()))),
    GET_USERS(new AllUsers(new UserServiceImpl(new UserDaoImpl())));

    private final Command command;

    CommandList(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return this.command;
    }
}
