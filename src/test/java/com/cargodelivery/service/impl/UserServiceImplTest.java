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

    @BeforeEach
    void setup() {
        testUser = new User("testId", "pass");
    }

    @Test
    void signupFailedWithUserAlreadyExistsTest() throws DBException {
        when(userRepoMock.isExist(any(User.class))).thenReturn(Boolean.TRUE);

        var exceptionMessage = assertThrows(UserServiceException.class, () -> userService.signup(testUser));
        assertEquals(String.format("User with id=%s already exist", testUser.getId()), exceptionMessage.getMessage());

        verify(userRepoMock, times(1)).isExist(any(User.class));
    }

    @Test
    void signupFailedWithUserCreateExceptionTest() throws DBException {
        doThrow(new DBException("[TEST]:User create operation failed")).when(userRepoMock).create(any(User.class));

        var exceptionMessage = assertThrows(UserServiceException.class, () -> userService.signup(testUser));
        assertTrue(exceptionMessage.getMessage().contains("[TEST]:User create operation failed"));

        verify(userRepoMock, times(1)).create(any(User.class));
    }

    @Test
    void signupSuccessfullyTest() throws DBException {
        doNothing().when(userRepoMock).create(any(User.class));

        assertDoesNotThrow(() -> userService.signup(testUser));

        verify(userRepoMock, times(1)).create(any(User.class));
    }

    @Test
    void loginSuccessfullyTest() throws DBException, NoSuchAlgorithmException, InvalidKeySpecException {
        when(userRepoMock.isExist(any(User.class))).thenReturn(Boolean.TRUE);
        when(userRepoMock.getPasswordById(any(User.class))).thenReturn(PasswordEncoder.hash(testUser.getPassword()));
        when(userRepoMock.findById(any(User.class))).thenReturn(Optional.of(testUser));

        var testLoggedUser = assertDoesNotThrow(() -> userService.login(testUser));
        assertEquals(testUser.getId(), testLoggedUser.getId());

        verify(userRepoMock, times(1)).isExist(any(User.class));
        verify(userRepoMock, times(1)).getPasswordById(any(User.class));
        verify(userRepoMock, times(1)).findById(any(User.class));
    }

    @Test
    void loginFailedWithInvalidPasswordExceptionTest() throws DBException {
        when(userRepoMock.isExist(any(User.class))).thenReturn(Boolean.TRUE);
        when(userRepoMock.getPasswordById(any(User.class))).thenThrow(new DBException("[TEST]:No such user was found"));

        var exceptionMessage = assertThrows(UserServiceException.class, () -> userService.login(testUser));
        assertTrue(exceptionMessage.getMessage().contains("[TEST]:No such user was found"));

        verify(userRepoMock, times(1)).getPasswordById(any(User.class));
    }

    @Test
    void loginFailedWithInvalidPasswordResultTest() throws DBException {
        when(userRepoMock.isExist(any(User.class))).thenReturn(Boolean.TRUE);
        when(userRepoMock.getPasswordById(any(User.class))).thenReturn("invalidPass");

        var exceptionMessage = assertThrows(UserServiceException.class, () -> userService.login(testUser));
        assertTrue(exceptionMessage.getMessage().contains("Invalid password. Didn't match with password from db"));

        verify(userRepoMock, times(1)).getPasswordById(any(User.class));
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
        assertTrue(exceptionMessage.getMessage().contains(String.format("User with provided login not exists, login=%s", testUser.getId())));

        verify(userRepoMock, times(1)).isExist(any(User.class));
    }

    @Test
    void findAllUsersSuccessfullyTest() throws DBException {
        doReturn(List.of(testUser)).when(userRepoMock).readAllUsers();

        var users = assertDoesNotThrow(userService::findAllUsers);
        assertTrue(users.contains(testUser));

        verify(userRepoMock, times(1)).readAllUsers();
    }

    @Test
    void findAllUsersFailedWithUserServiceExceptionTest() throws DBException {
        doThrow(new DBException("[TEST]:Failed to read all users")).when(userRepoMock).readAllUsers();

        var exceptionMessage = assertThrows(UserServiceException.class, userService::findAllUsers);
        assertTrue(exceptionMessage.getMessage().contains("[TEST]:Failed to read all users"));

        verify(userRepoMock, times(1)).readAllUsers();
    }

}
