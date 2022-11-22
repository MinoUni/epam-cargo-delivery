package com.cargodelivery.service.impl;

import com.cargodelivery.dao.entity.Cargo;
import com.cargodelivery.dao.entity.Order;
import com.cargodelivery.dao.entity.User;
import com.cargodelivery.dao.entity.enums.OrderState;
import com.cargodelivery.dao.entity.enums.UserRole;
import com.cargodelivery.dao.impl.OrderDaoImpl;
import com.cargodelivery.dao.impl.UserDaoImpl;
import com.cargodelivery.exception.DBException;
import com.cargodelivery.exception.OrderServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    private final OrderDaoImpl orderRepoMock = mock(OrderDaoImpl.class);
    private final UserDaoImpl userRepoMock = mock(UserDaoImpl.class);
    private final OrderServiceImpl orderService = new OrderServiceImpl(orderRepoMock, userRepoMock);
    private Cargo testCargo;
    private User testUser;
    private Order testOrder;

    @BeforeEach
    void setup() {
        testCargo = new Cargo(1, 11.1, 11.1, 11.1);
        testUser = new User("testId", "name", "surname", "email", "pass", new Date(10000), UserRole.USER);
        testOrder = new Order(1, 1, "route", testCargo, new Date(10000), new Date(30000), OrderState.WAITING_FOR_PAYMENT, BigDecimal.valueOf(10.00));
        testUser.setBalance(BigDecimal.valueOf(200));
    }

    @Test
    void findAllOrdersSuccessfullyTest() throws DBException {
        when(orderRepoMock.findAllOrders()).thenReturn(List.of(testOrder));

        var orders = assertDoesNotThrow(orderService::findAllOrders);
        assertTrue(orders.contains(testOrder));

        verify(orderRepoMock, times(1)).findAllOrders();
    }

    @Test
    void findAllOrdersFailedWithOrderServiceExceptionTest() throws DBException {
        when(orderRepoMock.findAllOrders()).thenThrow(new DBException("[TEST]:Failed to read all orders"));

        var exceptionMessage = assertThrows(OrderServiceException.class, orderService::findAllOrders);
        assertTrue(exceptionMessage.getMessage().contains("[TEST]:Failed to read all orders"));

        verify(orderRepoMock, times(1)).findAllOrders();
    }

    @Test
    void findAllUserOrdersSuccessfullyTest() throws DBException {
        when(userRepoMock.isExist(any(User.class))).thenReturn(Boolean.TRUE);
        when(orderRepoMock.findUserOrders(any(User.class))).thenReturn(List.of(testOrder));

        var userOrders = assertDoesNotThrow(() -> orderService.findAllUserOrders(testUser));
        assertTrue(userOrders.contains(testOrder));

        verify(userRepoMock, times(1)).isExist(any(User.class));
        verify(orderRepoMock, times(1)).findUserOrders(any(User.class));
    }

    @Test
    void findAllUserOrdersFailedWithNotExistExceptionTest() throws DBException {
        when(userRepoMock.isExist(any(User.class))).thenReturn(Boolean.FALSE);

        var excMessage = assertThrows(OrderServiceException.class, () -> orderService.findAllUserOrders(testUser));
        assertEquals("User not exist", excMessage.getMessage());

        verify(userRepoMock, times(1)).isExist(any(User.class));
        verify(orderRepoMock, times(0)).findUserOrders(any(User.class));
    }

    @Test
    void findAllUserOrdersFailedWithDBExceptionTest() throws DBException {
        when(userRepoMock.isExist(any(User.class))).thenReturn(Boolean.TRUE);
        when(orderRepoMock.findUserOrders(any(User.class))).thenThrow(new DBException("[TEST]:Failed to get user orders"));

        var excMessage = assertThrows(OrderServiceException.class, () -> orderService.findAllUserOrders(testUser));
        assertTrue(excMessage.getMessage().contains("[TEST]:Failed to get user orders"));

        verify(userRepoMock, times(1)).isExist(any(User.class));
        verify(orderRepoMock, times(1)).findUserOrders(any(User.class));
    }

    @Test
    void saveOrderSuccessfullyTest() throws DBException {
        doNothing().when(orderRepoMock).save(any(Order.class));

        assertDoesNotThrow(() -> orderService.saveOrder(testOrder));

        verify(orderRepoMock, times(1)).save(any(Order.class));
    }

    @Test
    void saveOrderFailedWithDBExceptionTest() throws DBException {
        doThrow(new DBException("[TEST]:Failed to save a new order")).when(orderRepoMock).save(any(Order.class));

        var excMessage = assertThrows(OrderServiceException.class, () -> orderService.saveOrder(testOrder));
        assertTrue(excMessage.getMessage().contains("[TEST]:Failed to save a new order"));

        verify(orderRepoMock, times(1)).save(any(Order.class));
    }

    @Test
    void deleteOrderFailedWithOrderIdNotFoundException() throws DBException {
        when(orderRepoMock.findByField(any(Integer.class))).thenReturn(Optional.empty());

        var exceptionMessage = assertThrows(OrderServiceException.class, () -> orderService.deleteOrder(testOrder.getId()));
        assertTrue(exceptionMessage.getMessage().contains(String.format(String.format("Order with orderId=[%s] not exist", testOrder.getId()))));

        verify(orderRepoMock, times(1)).findByField(any(Integer.class));
        verify(orderRepoMock, times(0)).deleteById(any(Integer.class));
    }

    @Test
    void deleteOrderFailedWithFindByFieldDBException() throws DBException {
        when(orderRepoMock.findByField(any(Integer.class))).thenThrow(new DBException("[TEST]:Failed to read order details from db"));

        var exceptionMessage = assertThrows(OrderServiceException.class, () -> orderService.deleteOrder(testOrder.getId()));
        assertTrue(exceptionMessage.getMessage().contains("[TEST]:Failed to read order details from db"));

        verify(orderRepoMock, times(1)).findByField(any(Integer.class));
        verify(orderRepoMock, times(0)).deleteById(any(Integer.class));
    }

    @Test
    void deleteOrderFailedWithDeleteByIdDBExceptionTest() throws DBException {
        when(orderRepoMock.findByField(any(Integer.class))).thenReturn(Optional.ofNullable(testOrder));
        doThrow(new DBException("[TEST]:Failed to delete order from db")).when(orderRepoMock).deleteById(any(Integer.class));

        var excMessage = assertThrows(OrderServiceException.class, () -> orderService.deleteOrder(testOrder.getId()));
        assertTrue(excMessage.getMessage().contains("[TEST]:Failed to delete order from db"));

        verify(orderRepoMock, times(1)).findByField(any(Integer.class));
        verify(orderRepoMock, times(1)).deleteById(any(Integer.class));
    }

    @Test
    void deleteOrderSuccessfullyTest() throws DBException {
        when(orderRepoMock.findByField(any(Integer.class))).thenReturn(Optional.ofNullable(testOrder));
        doNothing().when(orderRepoMock).deleteById(any(Integer.class));

        assertDoesNotThrow(() -> orderService.deleteOrder(testOrder.getId()));

        verify(orderRepoMock, times(1)).deleteById(any(Integer.class));
        verify(orderRepoMock, times(1)).deleteById(any(Integer.class));
    }

    @Test
    void updateStateSuccessfullyTest() throws DBException {
        var state = OrderState.WAITING_FOR_PAYMENT;

        when(orderRepoMock.findByField(any(Integer.class))).thenReturn(Optional.ofNullable(testOrder));
        doNothing().when(orderRepoMock).update(any(Order.class));

        assertDoesNotThrow(() -> orderService.updateState(testOrder.getId(), state));

        verify(orderRepoMock, times(1)).findByField(any(Integer.class));
        verify(orderRepoMock, times(1)).update(any(Order.class));
    }

    @Test
    void updateStateFailedFindByFieldDBExceptionTest() throws DBException {
        var state = OrderState.WAITING_FOR_PAYMENT;

        when(orderRepoMock.findByField(any(Integer.class))).thenThrow(new DBException("[TEST]:Failed to read order from db"));

        var exceptionMessage = assertThrows(OrderServiceException.class, () -> orderService.updateState(testOrder.getId(), state));
        assertTrue(exceptionMessage.getMessage().contains("[TEST]:Failed to read order from db"));

        verify(orderRepoMock, times(1)).findByField(any(Integer.class));
        verify(orderRepoMock, times(0)).update(any(Order.class));
    }

    @Test
    void updateStateFailedWithNotExistTest() throws DBException {
        OrderState state = OrderState.REGISTERED;
        when(orderRepoMock.findByField(any(Integer.class))).thenReturn(Optional.empty());

        var excMessage = assertThrows(OrderServiceException.class, () -> orderService.updateState(testOrder.getId(), state));
        assertEquals("No order was found in db", excMessage.getMessage());

        verify(orderRepoMock, times(1)).findByField(any(Integer.class));
        verify(orderRepoMock, times(0)).update(any(Order.class));
    }

    @Test
    void updateStateFailedWithUpdateDBExceptionTest() throws DBException {
        var state = OrderState.WAITING_FOR_PAYMENT;

        when(orderRepoMock.findByField(any(Integer.class))).thenReturn(Optional.ofNullable(testOrder));
        doThrow(new DBException("[TEST]:Failed to delete order from db")).when(orderRepoMock).update(any(Order.class));

        var excMessage = assertThrows(OrderServiceException.class, () -> orderService.updateState(testOrder.getId(), state));
        assertTrue(excMessage.getMessage().contains("[TEST]:Failed to delete order from db"));

        verify(orderRepoMock, times(1)).findByField(any(Integer.class));
        verify(orderRepoMock, times(1)).update(any(Order.class));
    }

    @Test
    void getOrdersLimitSuccessfullyTest() throws DBException {
        when(orderRepoMock.findAllBetween(any(int.class), any(int.class))).thenReturn(List.of(testOrder));
        var orders = assertDoesNotThrow(() -> orderService.getOrdersLimit(1));
        assertTrue(orders.contains(testOrder));

        verify(orderRepoMock, times(1)).findAllBetween(any(int.class), any(int.class));
    }

    @Test
    void getOrdersLimitFailedWithDBExceptionTest() throws DBException {
        when(orderRepoMock.findAllBetween(any(int.class), any(int.class))).thenThrow(new DBException("[TEST]:Failed to read orders limit from db"));

        var exceptionMessage = assertThrows(OrderServiceException.class, () -> orderService.getOrdersLimit(1));
        assertTrue(exceptionMessage.getMessage().contains("[TEST]:Failed to read orders limit from db"));

        verify(orderRepoMock, times(1)).findAllBetween(any(int.class), any(int.class));
    }

    @Test
    void getNumbOfPagesSuccessfullyTest() throws DBException {
        int numbOfRecords = 10;
        when(orderRepoMock.countNumbOfRecords(any(String.class))).thenReturn(numbOfRecords);

        var result = assertDoesNotThrow(orderService::getNumbOfPages);
        assertEquals(2, result);

        verify(orderRepoMock, times(1)).countNumbOfRecords(any(String.class));
    }

    @Test
    void getNumbOfPagesFailedWithDBExceptionTest() throws DBException {
        when(orderRepoMock.countNumbOfRecords(any(String.class))).thenThrow(new DBException("[TEST]:Failed to read number of records"));

        var exceptionMessage = assertThrows(OrderServiceException.class, orderService::getNumbOfPages);
        assertTrue(exceptionMessage.getMessage().contains("[TEST]:Failed to read number of records"));

        verify(orderRepoMock, times(1)).countNumbOfRecords(any(String.class));
    }

    @Test
    void findOrderSuccessfullyTest() throws DBException {
        when(orderRepoMock.findByField(any(Integer.class))).thenReturn(Optional.ofNullable(testOrder));

        var order = assertDoesNotThrow(() -> orderService.findOrder(testOrder.getId()));
        assertEquals(testOrder, order);

        verify(orderRepoMock, times(1)).findByField(any(Integer.class));
    }

    @Test
    void findOrderFailedWithDBExceptionTest() throws DBException {
        when(orderRepoMock.findByField(any(Integer.class))).thenThrow(new DBException("[TEST]:Failed to read order from db"));

        var exceptionMessage = assertThrows(OrderServiceException.class, () -> orderService.findOrder(testOrder.getId()));
        assertTrue(exceptionMessage.getMessage().contains("[TEST]:Failed to read order from db"));

        verify(orderRepoMock, times(1)).findByField(any(Integer.class));
    }

    @Test
    void findOrderFailedWithNotFoundTest() throws DBException {
        when(orderRepoMock.findByField(any(Integer.class))).thenReturn(Optional.empty());

        var exceptionMessage = assertThrows(OrderServiceException.class, () -> orderService.findOrder(testOrder.getId()));
        assertTrue(exceptionMessage.getMessage().contains("No order was found in db"));

        verify(orderRepoMock, times(1)).findByField(any(Integer.class));
    }

    @Test
    void payForOrderSuccessfullyTest() throws DBException {
        when(userRepoMock.findByField(any(String.class))).thenReturn(Optional.ofNullable(testUser));
        when(orderRepoMock.findByField(any(Integer.class))).thenReturn(Optional.ofNullable(testOrder));

        assertDoesNotThrow(() -> orderService.payForOrder(testOrder.getId(), testUser));

        verify(userRepoMock, times(1)).findByField(any(String.class));
        verify(orderRepoMock, times(1)).findByField(any(Integer.class));
    }

    @Test
    void payForOrderFailedWithInvalidOrderStateTest() throws DBException {
       var invalidOrder = new Order(1, 1, "route", testCargo, new Date(10000), new Date(30000), OrderState.PAID, BigDecimal.valueOf(10.00));

        when(userRepoMock.findByField(any(String.class))).thenReturn(Optional.ofNullable(testUser));
        when(orderRepoMock.findByField(any(Integer.class))).thenReturn(Optional.of(invalidOrder));

        var exception = assertThrows(OrderServiceException.class, () -> orderService.payForOrder(testOrder.getId(), testUser));
        assertTrue(exception.getMessage().contains(String.format("Invalid state=%s for order=%d", invalidOrder.getState().toString(), invalidOrder.getId())));

        verify(userRepoMock, times(1)).findByField(any(String.class));
        verify(orderRepoMock, times(1)).findByField(any(Integer.class));
    }

    @Test
    void payForOrderFailedWithUserNotFoundTest() throws DBException {
        when(userRepoMock.findByField(any(String.class))).thenReturn(Optional.empty());
        when(orderRepoMock.findByField(any(Integer.class))).thenReturn(Optional.ofNullable(testOrder));

        var exception = assertThrows(OrderServiceException.class, () -> orderService.payForOrder(testOrder.getId(), testUser));
        assertTrue(exception.getMessage().contains(String.format("User=%s not exists", testUser.getLogin())));

        verify(userRepoMock, times(1)).findByField(any(String.class));
        verify(orderRepoMock, times(1)).findByField(any(Integer.class));
    }

    @Test
    void payForOrderFailedWithOrderNotFoundTest() throws DBException {
        when(userRepoMock.findByField(any(String.class))).thenReturn(Optional.ofNullable(testUser));
        when(orderRepoMock.findByField(any(Integer.class))).thenReturn(Optional.empty());

        var exception = assertThrows(OrderServiceException.class, () -> orderService.payForOrder(testOrder.getId(), testUser));
        assertTrue(exception.getMessage().contains(String.format("Order=%d not exist", testOrder.getId())));

        verify(userRepoMock, times(1)).findByField(any(String.class));
        verify(orderRepoMock, times(1)).findByField(any(Integer.class));
    }

    @Test
    void payForOrderFailedWithUserLowBalanceTest() throws DBException {
        var invalidExpensiveOrder = new Order(1, 1, "route", testCargo, new Date(10000), new Date(30000), OrderState.WAITING_FOR_PAYMENT, BigDecimal.valueOf(999_999.00));
        when(userRepoMock.findByField(any(String.class))).thenReturn(Optional.ofNullable(testUser));
        when(orderRepoMock.findByField(any(Integer.class))).thenReturn(Optional.of(invalidExpensiveOrder));

        var exception = assertThrows(OrderServiceException.class, () -> orderService.payForOrder(testOrder.getId(), testUser));
        assertTrue(exception.getMessage().contains(String.format("User=%s balance too low to make a purchase", testUser.getLogin())));

        verify(userRepoMock, times(1)).findByField(any(String.class));
        verify(orderRepoMock, times(1)).findByField(any(Integer.class));
    }

    @Test
    void payForOrderFailedWithUserFindByFieldDBExceptionTest() throws DBException {
        when(userRepoMock.findByField(any(String.class))).thenThrow(new DBException("[TEST]:Failed to read user from db"));
        when(orderRepoMock.findByField(any(Integer.class))).thenReturn(Optional.ofNullable(testOrder));

        var exception = assertThrows(OrderServiceException.class, () -> orderService.payForOrder(testOrder.getId(), testUser));
        assertTrue(exception.getMessage().contains("[TEST]:Failed to read user from db"));

        verify(userRepoMock, times(1)).findByField(any(String.class));
        verify(orderRepoMock, times(0)).findByField(any(Integer.class));
    }
}
