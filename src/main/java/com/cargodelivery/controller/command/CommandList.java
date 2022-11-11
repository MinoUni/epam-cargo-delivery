package com.cargodelivery.controller.command;

import com.cargodelivery.controller.command.impl.get.*;
import com.cargodelivery.controller.command.impl.post.CreateOrder;
import com.cargodelivery.controller.command.impl.post.Login;
import com.cargodelivery.controller.command.impl.post.Signup;

public enum CommandList {

    ERROR_PAGE(new ErrorPage()),
    SIGNUP(new Signup()),
    LOGIN(new Login()),
    LOGOUT(new Logout()),
    LOCALE(new Locale()),
    CREATE_ORDER(new CreateOrder()),
    USER_ORDERS(new UserOrders()),
    ORDER_INFO(new OrderInfo()),
    ORDER_DELETE(new OrderDelete()),
    ORDER_PAY(new OrderPay()),
    ORDER_BILL(new OrderBill());

    private final Command command;

    CommandList(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }
}
