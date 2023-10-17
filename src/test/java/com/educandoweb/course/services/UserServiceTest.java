package com.educandoweb.course.services;

import com.educandoweb.course.entities.User;
import com.educandoweb.course.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataJpaTest
@ActiveProfiles("test")
class UserServiceTest {
    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        // Arrange
        // Arrange
        List<User> users = List.of(
                new User(1L, "Maria Brown", "maria@gmail.com", "988888888", "123456"),
                new User(2L, "Alex Green", "alex@gmail.com", "977777777", "1234567")
        );

        when(repository.findAll()).thenReturn(users);

        // Act
        List<User> usersTest = service.findAll();

        // Assert
        verify(repository, times(1)).findAll();
        assertEquals(users, usersTest);
    }

    private User createUser(String name, String email, String phone, String password) {
        // Arrange
        User newUser = new User(null, name, email, phone, password);

        // Act
        this.service.insert(newUser);

        // Assert
        return newUser;
    }

    @Test
    void findById() {
        // Arrange
        User u1 = new User(1L, "Maria Brown", "maria@gmail.com", "988888888", "123456");

        // Act
        when(repository.findById(u1.getId())).thenReturn(Optional.of(u1));
        Optional<User> u2 = this.repository.findById(u1.getId());

        // Assert
        verify(repository, times(1)).findById(1L);
        assertEquals(u1, u2.orElse(null));
    }

    @Test
    void insert() {
        // Arrange
        User user = new User(1L, "Maria Brown", "maria@gmail.com", "988888888", "123456");
        User userInserted = new User(1L, "Maria Brown", "maria@gmail.com", "988888888", "123456");

        when(repository.save(user)).thenReturn(userInserted);

        // Act
        User savedUser = service.insert(userInserted);

        // Assert
        verify(repository, times(1)).save(user);
        assertEquals(userInserted, savedUser);
    }

    @Test
    void delete() {
        //Arrange
        User user = new User(null, "Maria Brown", "maria@gmail.com", "988888888", "123456");

        // Act
        service.delete(user.getId());

        // Assert
        verify(repository, times(1)).deleteById(user.getId());
    }

    @Test
    void update() {
        // Arrange
        User userToUpdate = new User(1L, "Maria Brown", "maria@gmail.com", "988888888", "123456");
        User updatedUser = new User(1L, "Maria Green", "maria@gmail.com", "988888888", "123456");

        when(repository.findById(1L)).thenReturn(Optional.of(userToUpdate));
        when(repository.save(userToUpdate)).thenReturn(updatedUser);
        when(service.update(1L, updatedUser)).thenReturn(updatedUser);

        // Act
        User userTest = service.update(1L, updatedUser);

        // Assert
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(userToUpdate);
        assertEquals(updatedUser, userTest);
    }
}