package com.cargodelivery.controller.command;

import com.cargodelivery.controller.command.impl.get.*;
import com.cargodelivery.controller.command.impl.post.CreateOrder;
import com.cargodelivery.controller.command.impl.post.Login;
import com.cargodelivery.controller.command.impl.post.Signup;
import com.cargodelivery.dao.impl.OrderRepoImpl;
import com.cargodelivery.dao.impl.UserRepoImpl;
import com.cargodelivery.service.impl.OrderServiceImpl;
import com.cargodelivery.service.impl.UserServiceImpl;

public enum CommandList {

    ERROR_PAGE(new ErrorPage()),
    SIGNUP(new Signup(new UserServiceImpl(new UserRepoImpl()))),
    LOGIN(new Login(new UserServiceImpl(new UserRepoImpl()))),
    LOGOUT(new Logout()),
    LOCALE(new Locale()),

    CREATE_ORDER(new CreateOrder(new OrderServiceImpl(new OrderRepoImpl(), new UserRepoImpl()))),

    USER_ORDERS(new UserOrders(new OrderServiceImpl(new OrderRepoImpl(), new UserRepoImpl()))),

    ORDER_INFO(new OrderInfo(new OrderServiceImpl(new OrderRepoImpl(), new UserRepoImpl()),
            new UserOrders(new OrderServiceImpl(new OrderRepoImpl(), new UserRepoImpl())))),

    ORDER_DELETE(new OrderDelete(new OrderServiceImpl(new OrderRepoImpl(), new UserRepoImpl()),
            new UserOrders(new OrderServiceImpl(new OrderRepoImpl(), new UserRepoImpl())))),

    ORDER_PAY(new OrderPay(new OrderServiceImpl(new OrderRepoImpl(), new UserRepoImpl()),
            new UserOrders(new OrderServiceImpl(new OrderRepoImpl(), new UserRepoImpl())))),

    ORDER_BILL(new OrderBill(new OrderServiceImpl(new OrderRepoImpl(), new UserRepoImpl()),
            new UserServiceImpl(new UserRepoImpl()))),

    ORDER_APPROVE(new OrderApprove(new OrderServiceImpl(new OrderRepoImpl(), new UserRepoImpl()),
            new AllOrders(new OrderServiceImpl(new OrderRepoImpl(), new UserRepoImpl())))),

    ORDER_DECLINE(new OrderDecline(new OrderServiceImpl(new OrderRepoImpl(), new UserRepoImpl()),
            new AllOrders(new OrderServiceImpl(new OrderRepoImpl(), new UserRepoImpl())))),

    GET_ORDERS(new AllOrders(new OrderServiceImpl(new OrderRepoImpl(), new UserRepoImpl()))),
    GET_USERS(new AllUsers(new UserServiceImpl(new UserRepoImpl())));

    private final Command command;

    CommandList(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return this.command;
    }
}
