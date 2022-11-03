package com.cargodelivery.service.impl;

import com.cargodelivery.dao.entity.*;
import com.cargodelivery.dao.impl.CargoDaoImpl;
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

    private final CargoDaoImpl cargoRepoMock = mock(CargoDaoImpl.class);
    private final OrderDaoImpl orderRepoMock = mock(OrderDaoImpl.class);
    private final UserDaoImpl userRepoMock = mock(UserDaoImpl.class);
    private final OrderServiceImpl orderService = new OrderServiceImpl(cargoRepoMock, orderRepoMock, userRepoMock);
    private Order testOrder;
    private User testUser;
    private Cargo testCargo;

    @BeforeEach
    void setup() {
        testOrder = new Order(1, "testId", 1, BigDecimal.valueOf(10.00),
                "testRouteStart", "testRouteEnd",
                new Date(10000), new Date(30000), OrderState.REGISTERED);
        testUser = new User("testId", "name", "surname", "email", "pass", UserRole.USER, BigDecimal.valueOf(200));
        testCargo = new Cargo(1, 11.1, 11.1, 11.1);
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
        when(orderRepoMock.findAllOrdersByUserId(any(User.class))).thenReturn(List.of(testOrder));

        var userOrders = assertDoesNotThrow(() -> orderService.findAllUserOrders(testUser));
        assertTrue(userOrders.contains(testOrder));

        verify(userRepoMock, times(1)).isExist(any(User.class));
        verify(orderRepoMock, times(1)).findAllOrdersByUserId(any(User.class));
    }

    @Test
    void findAllUserOrdersFailedWithNotExistExceptionTest() throws DBException {
        when(userRepoMock.isExist(any(User.class))).thenReturn(Boolean.FALSE);

        var excMessage = assertThrows(OrderServiceException.class, () -> orderService.findAllUserOrders(testUser));
        assertEquals("User not exist", excMessage.getMessage());

        verify(userRepoMock, times(1)).isExist(any(User.class));
        verify(orderRepoMock, times(0)).findAllOrdersByUserId(any(User.class));
    }

    @Test
    void findAllUserOrdersFailedWithDBExceptionTest() throws DBException {
        when(userRepoMock.isExist(any(User.class))).thenReturn(Boolean.TRUE);
        when(orderRepoMock.findAllOrdersByUserId(any(User.class))).thenThrow(new DBException("[TEST]:Failed to get user orders"));

        var excMessage = assertThrows(OrderServiceException.class, () -> orderService.findAllUserOrders(testUser));
        assertTrue(excMessage.getMessage().contains("[TEST]:Failed to get user orders"));

        verify(userRepoMock, times(1)).isExist(any(User.class));
        verify(orderRepoMock, times(1)).findAllOrdersByUserId(any(User.class));
    }

    @Test
    void findCargoSuccessfullyTest() throws DBException {
        String cargoId = "1";

        when(cargoRepoMock.isExist(any(Integer.class))).thenReturn(Boolean.TRUE);
        when(cargoRepoMock.findById(any(Integer.class))).thenReturn(Optional.ofNullable(testCargo));

        var cargo = assertDoesNotThrow(() -> orderService.findCargo(cargoId));
        assertNotNull(cargo);
        assertEquals(cargo.getId(), testCargo.getId());

        verify(cargoRepoMock, times(1)).isExist(any(Integer.class));
        verify(cargoRepoMock, times(1)).findById(any(Integer.class));
    }

    @Test
    void findCargoFailedWithParseExceptionTest() throws DBException {
        String cargoId = "cargoId";
        var exceptionMessage = assertThrows(OrderServiceException.class, () -> orderService.findCargo(cargoId));
        assertEquals(String.format("Invalid to parse param=%s", cargoId), exceptionMessage.getMessage());
        verify(cargoRepoMock, times(0)).isExist(any(Integer.class));
    }

    @Test
    void findCargoFailedWithNotExistExceptionTest() throws DBException {
        String cargoId = "9999";

        when(cargoRepoMock.isExist(any(Integer.class))).thenReturn(Boolean.FALSE);

        var exceptionMessage = assertThrows(OrderServiceException.class, () -> orderService.findCargo(cargoId));
        assertEquals(String.format("Cargo with cargoId=[%s] not exist", cargoId), exceptionMessage.getMessage());

        verify(cargoRepoMock, times(1)).isExist(any(Integer.class));
        verify(cargoRepoMock, times(0)).findById(any(Integer.class));
    }

    @Test
    void findCargoFailedWithDBExceptionTest() throws DBException {
        String cargoId = "8888";

        when(cargoRepoMock.isExist(any(Integer.class))).thenReturn(Boolean.TRUE);
        when(cargoRepoMock.findById(any(Integer.class))).thenThrow(new DBException("[TEST]:Failed to get a cargo by it id from database"));

        var exceptionMessage = assertThrows(OrderServiceException.class, () -> orderService.findCargo(cargoId));
        assertTrue(exceptionMessage.getMessage().contains("[TEST]:Failed to get a cargo by it id from database"));

        verify(cargoRepoMock, times(1)).isExist(any(Integer.class));
        verify(cargoRepoMock, times(1)).findById(any(Integer.class));
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
    void saveCargoSuccessfullyTest() throws DBException {
        when(cargoRepoMock.save(any(Cargo.class))).thenReturn(testCargo);

        var cargo = assertDoesNotThrow(() -> orderService.saveCargo(testCargo));
        assertEquals(testCargo.getId(), cargo.getId());

        verify(cargoRepoMock, times(1)).save(any(Cargo.class));
    }

    @Test
    void saveCargoFailedWithDBException() throws DBException {
        when(cargoRepoMock.save(any(Cargo.class))).thenThrow(new DBException("[TEST]:Failed to save a new cargo"));

        var excMessage = assertThrows(OrderServiceException.class, () -> orderService.saveCargo(testCargo));
        assertTrue(excMessage.getMessage().contains("[TEST]:Failed to save a new cargo"));

        verify(cargoRepoMock, times(1)).save(any(Cargo.class));
    }

    @Test
    void deleteOrderFailedWithOrderIdOrCargoIdParseException() throws DBException {
        String invalidOrderId = "invalidOrder";
        String validOrderId = "4444";
        String cargoId = "invalidCargo";

        var excOrderMessage = assertThrows(OrderServiceException.class, () -> orderService.deleteOrder(invalidOrderId, cargoId));
        assertEquals(String.format("Invalid to parse param=%s", invalidOrderId),excOrderMessage.getMessage());

        var excCargoMessage = assertThrows(OrderServiceException.class, () -> orderService.deleteOrder(validOrderId, cargoId));
        assertEquals(String.format("Invalid to parse param=%s", cargoId),excCargoMessage.getMessage());

        verify(orderRepoMock, times(0)).deleteById(any(Integer.class));
        verify(orderRepoMock, times(0)).isExist(any(Integer.class));
    }

    @Test
    void deleteOrderFailedWithNotExistExceptionTest() throws DBException {
        String orderId = "9999";
        String cargoId = "9999";

        when(orderRepoMock.isExist(any(Integer.class))).thenReturn(Boolean.FALSE);

        var exceptionMessage = assertThrows(OrderServiceException.class, () -> orderService.deleteOrder(orderId, cargoId));
        assertEquals(String.format("Order with orderId=[%s] not exist", orderId), exceptionMessage.getMessage());

        verify(orderRepoMock, times(1)).isExist(any(Integer.class));
        verify(orderRepoMock, times(0)).deleteById(any(Integer.class));
    }

    @Test
    void deleteOrderFailedWithOrderDBExceptionTest() throws DBException {
        String orderId = "3333";
        String cargoId = "3333";

        when(orderRepoMock.isExist(any(Integer.class))).thenReturn(Boolean.TRUE);
        doThrow(new DBException(String.format("[TEST]:Failed to delete order with orderId=%s", orderId))).when(orderRepoMock).deleteById(any(Integer.class));

        var excMessage = assertThrows(OrderServiceException.class, () -> orderService.deleteOrder(orderId, cargoId));
        assertTrue(excMessage.getMessage().contains(String.format("[TEST]:Failed to delete order with orderId=%s", orderId)));

        verify(orderRepoMock, times(1)).isExist(any(Integer.class));
        verify(orderRepoMock, times(1)).deleteById(any(Integer.class));
        verify(orderRepoMock, times(0)).isExistOrderWithCargoId(any(Integer.class));
    }

    @Test
    void deleteOrderFailedWithExistAnotherOrderWithCargoDBExceptionTest() throws DBException {
        String orderId = "2222";
        String cargoId = "2222";

        when(orderRepoMock.isExist(any(Integer.class))).thenReturn(Boolean.TRUE);
        doNothing().when(orderRepoMock).deleteById(any(Integer.class));
        when(orderRepoMock.isExistOrderWithCargoId(any(Integer.class)))
                .thenThrow(new DBException(String.format("[TEST]:Failed to check order=%s existence with cargoId=%s", orderId, cargoId)));

        var message = assertThrows(OrderServiceException.class, () -> orderService.deleteOrder(orderId, cargoId));
        assertTrue(message.getMessage().contains(String.format("[TEST]:Failed to check order=%s existence with cargoId=%s", orderId, cargoId)));

        verify(orderRepoMock, times(1)).isExist(any(Integer.class));
        verify(orderRepoMock, times(1)).deleteById(any(Integer.class));
        verify(orderRepoMock, times(1)).isExistOrderWithCargoId(any(Integer.class));
        verify(cargoRepoMock, times(0)).deleteById(any(Integer.class));
    }

    @Test
    void deleteOrderFailedWithExistAnotherOrderWithCargoTest() throws DBException {
        String orderId = "8546";
        String cargoId = "8546";

        when(orderRepoMock.isExist(any(Integer.class))).thenReturn(Boolean.TRUE);
        doNothing().when(orderRepoMock).deleteById(any(Integer.class));
        when(orderRepoMock.isExistOrderWithCargoId(any(Integer.class))).thenReturn(Boolean.TRUE);

        var message = assertThrows(OrderServiceException.class, () -> orderService.deleteOrder(orderId, cargoId));
        assertTrue(message.getMessage().contains(String.format("There are another order with that cargo=%s", cargoId)));

        verify(orderRepoMock, times(1)).isExist(any(Integer.class));
        verify(orderRepoMock, times(1)).deleteById(any(Integer.class));
        verify(orderRepoMock, times(1)).isExistOrderWithCargoId(any(Integer.class));
        verify(cargoRepoMock, times(0)).deleteById(any(Integer.class));
    }

    @Test
    void deleteOrderFailedWithDeleteCargoDBExceptionTest() throws DBException {
        String orderId = "8546";
        String cargoId = "8546";

        when(orderRepoMock.isExist(any(Integer.class))).thenReturn(Boolean.TRUE);
        doNothing().when(orderRepoMock).deleteById(any(Integer.class));
        when(orderRepoMock.isExistOrderWithCargoId(any(Integer.class))).thenReturn(Boolean.FALSE);
        doThrow(new DBException(String.format("[TEST]:Failed to delete cargo=%s", cargoId))).when(cargoRepoMock).deleteById(any(Integer.class));

        var message = assertThrows(OrderServiceException.class, () -> orderService.deleteOrder(orderId, cargoId));
        assertTrue(message.getMessage().contains(String.format("[TEST]:Failed to delete cargo=%s", cargoId)));

        verify(orderRepoMock, times(1)).isExist(any(Integer.class));
        verify(orderRepoMock, times(1)).deleteById(any(Integer.class));
        verify(orderRepoMock, times(1)).isExistOrderWithCargoId(any(Integer.class));
        verify(cargoRepoMock, times(1)).deleteById(any(Integer.class));

    }

    @Test
    void deleteOrderSuccessfullyTest() throws DBException {
        String orderId = "8546";
        String cargoId = "8546";

        when(orderRepoMock.isExist(any(Integer.class))).thenReturn(Boolean.TRUE);
        doNothing().when(orderRepoMock).deleteById(any(Integer.class));
        when(orderRepoMock.isExistOrderWithCargoId(any(Integer.class))).thenReturn(Boolean.FALSE);
        doNothing().when(cargoRepoMock).deleteById(any(Integer.class));

        assertDoesNotThrow(() -> orderService.deleteOrder(orderId, cargoId));

        verify(orderRepoMock, times(1)).isExist(any(Integer.class));
        verify(orderRepoMock, times(1)).deleteById(any(Integer.class));
        verify(orderRepoMock, times(1)).isExistOrderWithCargoId(any(Integer.class));
        verify(cargoRepoMock, times(1)).deleteById(any(Integer.class));
    }

    @Test
    void updateOrderStateSuccessfullyTest() throws DBException {
        String orderId = "99221";
        OrderState state = OrderState.REGISTERED;

        when(orderRepoMock.isExist(any(Integer.class))).thenReturn(Boolean.TRUE);
        doNothing().when(orderRepoMock).updateOrderState(any(Integer.class), any(OrderState.class));

        assertDoesNotThrow(() -> orderService.updateOrderState(orderId, state));

        verify(orderRepoMock, times(1)).isExist(any(Integer.class));
        verify(orderRepoMock, times(1)).updateOrderState(any(Integer.class), any(OrderState.class));
    }

    @Test
    void updateOrderStateFailedWithParseExceptionTest() throws DBException {
        String orderId = "invalidValue";
        OrderState state = OrderState.REGISTERED;

        var exceptionMessage = assertThrows(OrderServiceException.class, () -> orderService.updateOrderState(orderId, state));

        assertEquals(String.format("Invalid to parse param=%s", orderId), exceptionMessage.getMessage());

        verify(orderRepoMock, times(0)).isExist(any(Integer.class));
    }

    @Test
    void updateOrderStateFailedWithNotExistTest() throws DBException {
        String orderId = "9922";
        OrderState state = OrderState.REGISTERED;

        when(orderRepoMock.isExist(any(Integer.class))).thenReturn(Boolean.FALSE);

        var excMessage = assertThrows(OrderServiceException.class, () -> orderService.updateOrderState(orderId, state));
        assertEquals(String.format("Order with orderId=%s not exist", orderId), excMessage.getMessage());

        verify(orderRepoMock, times(1)).isExist(any(Integer.class));
        verify(orderRepoMock, times(0)).updateOrderState(any(Integer.class), any(OrderState.class));
    }

    @Test
    void updateOrderStateFailedWithDBExceptionTest() throws DBException {
        String orderId = "99221";
        OrderState state = OrderState.REGISTERED;

        when(orderRepoMock.isExist(any(Integer.class))).thenReturn(Boolean.TRUE);
        doThrow(new DBException("[TEST]:Failed to update order state")).when(orderRepoMock).updateOrderState(any(Integer.class), any(OrderState.class));

        var excMessage = assertThrows(OrderServiceException.class, () -> orderService.updateOrderState(orderId, state));
        assertTrue(excMessage.getMessage().contains("[TEST]:Failed to update order state"));

        verify(orderRepoMock, times(1)).isExist(any(Integer.class));
        verify(orderRepoMock, times(1)).updateOrderState(any(Integer.class), any(OrderState.class));
    }

    @Test
    void payForOrderFailedWithParseExceptionTest() throws DBException {
        String orderId = "invalidId";

        var excMessage = assertThrows(OrderServiceException.class, () -> orderService.payForOrder(orderId, testUser));
        assertEquals(String.format("Invalid to parse param=%s", orderId), excMessage.getMessage());

        verify(userRepoMock, times(0)).isExist(any(User.class));
    }

    @Test
    void payForOrderFailedWithUserNotExistTest() throws DBException {
        String orderId = "5351";

        when(userRepoMock.isExist(any(User.class))).thenReturn(Boolean.FALSE);

        var excMessage = assertThrows(OrderServiceException.class, () -> orderService.payForOrder(orderId, testUser));
        assertEquals(String.format("User with login=[%s] not exists", testUser.getId()), excMessage.getMessage());

        verify(userRepoMock, times(1)).isExist(any(User.class));
        verify(orderRepoMock, times(0)).isExist(any(Integer.class));
    }

    @Test
    void payForOrderFailedWithOrderNotExistTest() throws DBException {
        String orderId = "42256";

        when(userRepoMock.isExist(any(User.class))).thenReturn(Boolean.TRUE);
        when(orderRepoMock.isExist(any(Integer.class))).thenReturn(Boolean.FALSE);

        var excMessage = assertThrows(OrderServiceException.class, () -> orderService.payForOrder(orderId, testUser));
        assertEquals(String.format("Order with orderId=[%s] not exist", orderId), excMessage.getMessage());

        verify(userRepoMock, times(1)).isExist(any(User.class));
        verify(orderRepoMock, times(1)).isExist(any(Integer.class));
        verify(userRepoMock, times(0)).getUserBalance(any(User.class));
    }

    @Test
    void payForOrderFailedWithGetUserBalanceDBExceptionTest() throws DBException {
        String orderId = "42256";

        when(userRepoMock.isExist(any(User.class))).thenReturn(Boolean.TRUE);
        when(orderRepoMock.isExist(any(Integer.class))).thenReturn(Boolean.TRUE);
        when(userRepoMock.getUserBalance(any(User.class))).thenThrow(new DBException(String.format("[TEST]:Failed to get balance for user=%s from db", testUser.getId())));

        var excMessage = assertThrows(OrderServiceException.class, () -> orderService.payForOrder(orderId, testUser));
        assertTrue(excMessage.getMessage().contains(String.format("[TEST]:Failed to get balance for user=%s from db", testUser.getId())));

        verify(userRepoMock, times(1)).isExist(any(User.class));
        verify(orderRepoMock, times(1)).isExist(any(Integer.class));
        verify(userRepoMock, times(1)).getUserBalance(any(User.class));
        verify(orderRepoMock, times(0)).findOrderPrice(any(Integer.class));
    }

    @Test
    void payForOrderFailedWithGetOrderPriceDBExceptionTest() throws DBException {
        String orderId = "42256";

        when(userRepoMock.isExist(any(User.class))).thenReturn(Boolean.TRUE);
        when(orderRepoMock.isExist(any(Integer.class))).thenReturn(Boolean.TRUE);
        when(userRepoMock.getUserBalance(any(User.class))).thenReturn(BigDecimal.valueOf(10));
        when(orderRepoMock.findOrderPrice(any(Integer.class))).thenThrow(new DBException(String.format("[TEST]:Failed to get price for order=%s from db", orderId)));

        var excMessage = assertThrows(OrderServiceException.class, () -> orderService.payForOrder(orderId, testUser));
        assertTrue(excMessage.getMessage().contains(String.format("[TEST]:Failed to get price for order=%s from db", orderId)));

        verify(userRepoMock, times(1)).isExist(any(User.class));
        verify(orderRepoMock, times(1)).isExist(any(Integer.class));
        verify(userRepoMock, times(1)).getUserBalance(any(User.class));
        verify(orderRepoMock, times(1)).findOrderPrice(any(Integer.class));
        verify(userRepoMock, times(0)).updateUserBalance(any(User.class), any(BigDecimal.class));
    }

    @Test
    void payForOrderFailedWithSubtractOperationTest() throws DBException {
        String orderId = "42256";

        when(userRepoMock.isExist(any(User.class))).thenReturn(Boolean.TRUE);
        when(orderRepoMock.isExist(any(Integer.class))).thenReturn(Boolean.TRUE);
        when(userRepoMock.getUserBalance(any(User.class))).thenReturn(BigDecimal.valueOf(10));
        when(orderRepoMock.findOrderPrice(any(Integer.class))).thenReturn(BigDecimal.valueOf(100));

        var excMessage = assertThrows(OrderServiceException.class, () -> orderService.payForOrder(orderId, testUser));
        assertTrue(excMessage.getMessage().contains("Low balance"));

        verify(userRepoMock, times(1)).isExist(any(User.class));
        verify(orderRepoMock, times(1)).isExist(any(Integer.class));
        verify(userRepoMock, times(1)).getUserBalance(any(User.class));
        verify(orderRepoMock, times(1)).findOrderPrice(any(Integer.class));
        verify(userRepoMock, times(0)).updateUserBalance(any(User.class), any(BigDecimal.class));
    }

    @Test
    void payForOrderFailedWithUpdateUserBalanceOperationTest() throws DBException {
        String orderId = "42256";

        when(userRepoMock.isExist(any(User.class))).thenReturn(Boolean.TRUE);
        when(orderRepoMock.isExist(any(Integer.class))).thenReturn(Boolean.TRUE);
        when(userRepoMock.getUserBalance(any(User.class))).thenReturn(BigDecimal.valueOf(100));
        when(orderRepoMock.findOrderPrice(any(Integer.class))).thenReturn(BigDecimal.valueOf(50));
        doThrow(new DBException("[TEST]:Failed to update balance for user")).when(userRepoMock).updateUserBalance(any(User.class), any(BigDecimal.class));

        var excMessage = assertThrows(OrderServiceException.class, () -> orderService.payForOrder(orderId, testUser));
        assertTrue(excMessage.getMessage().contains("[TEST]:Failed to update balance for user"));

        verify(userRepoMock, times(1)).isExist(any(User.class));
        verify(orderRepoMock, times(1)).isExist(any(Integer.class));
        verify(userRepoMock, times(1)).getUserBalance(any(User.class));
        verify(orderRepoMock, times(1)).findOrderPrice(any(Integer.class));
        verify(userRepoMock, times(1)).updateUserBalance(any(User.class), any(BigDecimal.class));
        verify(orderRepoMock, times(0)).updateOrderState(any(Integer.class), any(OrderState.class));
    }

    @Test
    void payForOrderFailedWithUpdateOrderStateOperationTest() throws DBException {
        String orderId = "42256";

        when(userRepoMock.isExist(any(User.class))).thenReturn(Boolean.TRUE);
        when(orderRepoMock.isExist(any(Integer.class))).thenReturn(Boolean.TRUE);
        when(userRepoMock.getUserBalance(any(User.class))).thenReturn(BigDecimal.valueOf(100));
        when(orderRepoMock.findOrderPrice(any(Integer.class))).thenReturn(BigDecimal.valueOf(50));
        doNothing().when(userRepoMock).updateUserBalance(any(User.class), any(BigDecimal.class));
        doThrow(new DBException("[TEST]:Failed to update state for order")).when(orderRepoMock).updateOrderState(any(Integer.class), any(OrderState.class));

        var excMessage = assertThrows(OrderServiceException.class, () -> orderService.payForOrder(orderId, testUser));
        assertTrue(excMessage.getMessage().contains("[TEST]:Failed to update state for order"));

        verify(userRepoMock, times(1)).isExist(any(User.class));
        verify(orderRepoMock, times(1)).isExist(any(Integer.class));
        verify(userRepoMock, times(1)).getUserBalance(any(User.class));
        verify(orderRepoMock, times(1)).findOrderPrice(any(Integer.class));
        verify(userRepoMock, times(1)).updateUserBalance(any(User.class), any(BigDecimal.class));
        verify(orderRepoMock, times(1)).updateOrderState(any(Integer.class), any(OrderState.class));
    }

    @Test
    void payForOrderSuccessfullyTest() throws DBException {
        String orderId = "42256";

        when(userRepoMock.isExist(any(User.class))).thenReturn(Boolean.TRUE);
        when(orderRepoMock.isExist(any(Integer.class))).thenReturn(Boolean.TRUE);
        when(userRepoMock.getUserBalance(any(User.class))).thenReturn(testUser.getBalance());
        when(orderRepoMock.findOrderPrice(any(Integer.class))).thenReturn(testOrder.getPrice());
        doNothing().when(userRepoMock).updateUserBalance(any(User.class), any(BigDecimal.class));
        doNothing().when(orderRepoMock).updateOrderState(any(Integer.class), any(OrderState.class));
        when(userRepoMock.findById(any(User.class))).thenReturn(Optional.ofNullable(testUser));

        var userAfterPayment = assertDoesNotThrow(() -> orderService.payForOrder(orderId, testUser));
        assertNotNull(userAfterPayment);
        assertEquals(testUser.getId(), userAfterPayment.getId());

        verify(userRepoMock, times(1)).isExist(any(User.class));
        verify(orderRepoMock, times(1)).isExist(any(Integer.class));
        verify(userRepoMock, times(1)).getUserBalance(any(User.class));
        verify(orderRepoMock, times(1)).findOrderPrice(any(Integer.class));
        verify(userRepoMock, times(1)).updateUserBalance(any(User.class), any(BigDecimal.class));
        verify(orderRepoMock, times(1)).updateOrderState(any(Integer.class), any(OrderState.class));
        verify(userRepoMock, times(1)).findById(any(User.class));
    }

}
