package com.educandoweb.course.repositories;

import com.educandoweb.course.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

class UserRepositoryTest {
    @Mock
    private UserRepository repository;

    @Test
    @DisplayName("Should return Maria Brown and Alex Green informations")
    void findAllUsers() {
        // Arrange
        List<User> users = new ArrayList<>();
        User u1 = new User(null, "Maria Brown", "maria@gmail.com", "988888888", "123456");
        User u2 = new User(null, "Alex Green", "alex@gmail.com", "977777777", "1234567");

        when(createUser(u1.getName(), u1.getEmail(), u1.getPhone(), u1.getPassword())).thenReturn(u1);
        when(createUser(u2.getName(), u2.getEmail(), u2.getPhone(), u2.getPassword())).thenReturn(u2);

        users.add(u1);
        users.add(u2);

        createUser(u1.getName(), u1.getEmail(), u1.getPhone(), u1.getPassword());
        createUser(u2.getName(), u2.getEmail(), u2.getPhone(), u2.getPassword());

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

        // Act
        List<User> usersTest = repository.findAll();

        // Assert
        Assertions.assertEquals(users.stream().map(User::toString).collect(Collectors.toList()), usersTest.stream().map(User::toString).collect(Collectors.toList()));
    }

    private User createUser(String name, String email, String phone, String password) {
        var newUser = new User(null, name, email, phone, password);
        this.repository.save(newUser);
        return newUser;
    }
}