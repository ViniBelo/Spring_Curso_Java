package com.educandoweb.course.repositories;

import com.educandoweb.course.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserRepositoryTest {
    private final UserRepository repository = mock(UserRepository.class);

    @Test
    @DisplayName("Should return Maria Brown and Alex Green informations")
    void findAllUsers() {
        // Arrange
        List<User> users = new ArrayList<>();
        User u1 = new User(null, "Maria Brown", "maria@gmail.com", "988888888", "123456");
        User u2 = new User(null, "Alex Green", "alex@gmail.com", "977777777", "1234567");

        users.add(u1);
        users.add(u2);

        when(repository.findAll()).thenReturn(users);

        // Act
        List<User> usersTest = repository.findAll();

        // Assert
        Assertions.assertEquals(users.stream().map(User::toString).collect(Collectors.toList()), usersTest.stream().map(User::toString).collect(Collectors.toList()));
    }

    @Test
    @DisplayName("Should return nothing")
    void findAllUsersEmpty() {
        // Arrange
        List<User> users = new ArrayList<>();

        when(repository.findAll()).thenReturn(users);

        // Act
        List<User> usersTest = repository.findAll();

        // Assert
        Assertions.assertEquals(users.stream().map(User::toString).collect(Collectors.toList()), usersTest.stream().map(User::toString).collect(Collectors.toList()));
    }
}