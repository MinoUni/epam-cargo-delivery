package com.cargodelivery.service.impl;

import com.cargodelivery.dao.entity.User;
import com.cargodelivery.dao.impl.UserDaoImpl;
import com.cargodelivery.exception.DBException;
import com.cargodelivery.exception.UserServiceException;
import com.cargodelivery.security.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private final UserDaoImpl userRepoMock = mock(UserDaoImpl.class);
    private final UserServiceImpl userService = new UserServiceImpl(userRepoMock);
    private User testUser;
    private User testUserData;

    @BeforeEach
    void setup() throws NoSuchAlgorithmException, InvalidKeySpecException {
        testUser = new User("testId", "pass");
        testUserData = new User("testId", PasswordEncoder.hash(testUser.getPassword()));
    }

    @Test
    void signupFailedWithUserAlreadyExistsTest() throws DBException {
        when(userRepoMock.isExist(any(User.class))).thenReturn(Boolean.TRUE);

        var exceptionMessage = assertThrows(UserServiceException.class, () -> userService.signup(testUser));
        assertEquals(String.format("User with id=%s already exist", testUser.getId()), exceptionMessage.getMessage());

        verify(userRepoMock, times(1)).isExist(any(User.class));
    }

    @Test
    void signupFailedWithUserExceptionTest() throws DBException {
        doThrow(new DBException("[TEST]:User  operation failed")).when(userRepoMock).save(any(User.class));

        var exceptionMessage = assertThrows(UserServiceException.class, () -> userService.signup(testUser));
        assertTrue(exceptionMessage.getMessage().contains("[TEST]:User  operation failed"));

        verify(userRepoMock, times(1)).save(any(User.class));
    }

    @Test
    void signupSuccessfullyTest() throws DBException {
        doNothing().when(userRepoMock).save(any(User.class));

        assertDoesNotThrow(() -> userService.signup(testUser));

        verify(userRepoMock, times(1)).save(any(User.class));
    }

    @Test
    void loginSuccessfullyTest() throws DBException {
        when(userRepoMock.isExist(any(User.class))).thenReturn(Boolean.TRUE);
        when(userRepoMock.findByField(any(String.class))).thenReturn(Optional.ofNullable(testUserData));

        var testLoggedUser = assertDoesNotThrow(() -> userService.login(testUser));
        assertEquals(testUser.getId(), testLoggedUser.getId());

        verify(userRepoMock, times(1)).isExist(any(User.class));
        verify(userRepoMock, times(1)).findByField(any(String.class));
    }

    @Test
    void loginFailedWithInvalidPasswordExceptionTest() throws DBException {
        when(userRepoMock.isExist(any(User.class))).thenReturn(Boolean.TRUE);
        when(userRepoMock.findByField(any(String.class))).thenThrow(new DBException("[TEST]:No such user was found"));

        var exceptionMessage = assertThrows(UserServiceException.class, () -> userService.login(testUser));
        assertTrue(exceptionMessage.getMessage().contains("[TEST]:No such user was found"));

        verify(userRepoMock, times(1)).findByField(any(String.class));
    }

    @Test
    void loginFailedWithInvalidPasswordResultTest() throws DBException {
        when(userRepoMock.isExist(any(User.class))).thenReturn(Boolean.TRUE);
        when(userRepoMock.findByField(any(String.class))).thenReturn(Optional.ofNullable(testUser));

        var exceptionMessage = assertThrows(UserServiceException.class, () -> userService.login(testUser));
        assertTrue(exceptionMessage.getMessage().contains("Invalid password. Didn't match with password from db"));

        verify(userRepoMock, times(1)).findByField(any(String.class));
    }

    @Test
    void loginFailedWithUserNotExistExceptionTest() throws DBException {
        when(userRepoMock.isExist(any(User.class))).thenThrow(new DBException("[TEST]:No such user was found"));

        var exceptionMessage = assertThrows(UserServiceException.class, () -> userService.login(testUser));
        assertTrue(exceptionMessage.getMessage().contains("[TEST]:No such user was found"));

        verify(userRepoMock, times(1)).isExist(any(User.class));
    }

    @Test
    void loginFailedWithUserNotExistResultTest() throws DBException {
        when(userRepoMock.isExist(any(User.class))).thenReturn(Boolean.FALSE);

        var exceptionMessage = assertThrows(UserServiceException.class, () -> userService.login(testUser));
        assertTrue(exceptionMessage.getMessage().contains(String.format("User with provided login not exists, login=%s", testUser.getLogin())));

        verify(userRepoMock, times(1)).isExist(any(User.class));
    }

    @Test
    void findAllUsersSuccessfullyTest() throws DBException {
        when(userRepoMock.findAllBetween(any(int.class), any(int.class))).thenReturn(List.of(testUser));
        var users = assertDoesNotThrow(() -> userService.findAllUsers(1));
        assertTrue(users.contains(testUser));

        verify(userRepoMock, times(1)).findAllBetween(any(int.class), any(int.class));
    }

    @Test
    void findAllUsersFailedWithUserServiceExceptionTest() throws DBException {
        when(userRepoMock.findAllBetween(any(int.class), any(int.class))).thenThrow(new DBException("[TEST]:Failed to read all users"));
        var exceptionMessage = assertThrows(UserServiceException.class, () -> userService.findAllUsers(1));
        assertTrue(exceptionMessage.getMessage().contains("[TEST]:Failed to read all users"));

        verify(userRepoMock, times(1)).findAllBetween(any(int.class), any(int.class));
    }

    @Test
    void getNumbOfPagesSuccessfullyTest() throws DBException {
        int numbOfRecords = 10;
        when(userRepoMock.countNumbOfRecords(any(String.class))).thenReturn(numbOfRecords);

        var result = assertDoesNotThrow(userService::getNumbOfPages);
        assertEquals(2, result);

        verify(userRepoMock, times(1)).countNumbOfRecords(any(String.class));
    }

    @Test
    void getNumbOfPagesFailedWithDBExceptionTest() throws DBException {
        when(userRepoMock.countNumbOfRecords(any(String.class))).thenThrow(new DBException("[TEST]:Failed to read number of records"));

        var exceptionMessage = assertThrows(UserServiceException.class, userService::getNumbOfPages);
        assertTrue(exceptionMessage.getMessage().contains("[TEST]:Failed to read number of records"));

        verify(userRepoMock, times(1)).countNumbOfRecords(any(String.class));
    }

    @Test
    void findUserSuccessfullyTest() throws DBException {
        when(userRepoMock.findByField(any(String.class))).thenReturn(Optional.ofNullable(testUserData));

        var user = assertDoesNotThrow(() -> userService.findUser(testUser));
        assertEquals(testUser, user);

        verify(userRepoMock, times(1)).findByField(any(String.class));
    }

    @Test
    void findUserFailedWithDBExceptionTest() throws DBException {
        when(userRepoMock.findByField(any(String.class))).thenThrow(new DBException("[TEST]:Failed to read user from db"));

        var exceptionMessage = assertThrows(UserServiceException.class, () -> userService.findUser(testUser));
        assertTrue(exceptionMessage.getMessage().contains("[TEST]:Failed to read user from db"));

        verify(userRepoMock, times(1)).findByField(any(String.class));
    }

    @Test
    void findUserFailedWithNotFoundTest() throws DBException {
        when(userRepoMock.findByField(any(String.class))).thenReturn(Optional.empty());

        var exceptionMessage = assertThrows(UserServiceException.class, () -> userService.findUser(testUser));
        assertTrue(exceptionMessage.getMessage().contains(String.format("User=%s not exists", testUser.getLogin())));

        verify(userRepoMock, times(1)).findByField(any(String.class));
    }
}
