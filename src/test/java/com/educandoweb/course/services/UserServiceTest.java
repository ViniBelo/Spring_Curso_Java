package com.educandoweb.course.services;

import com.educandoweb.course.entities.User;
import com.educandoweb.course.repositories.UserRepository;
import com.educandoweb.course.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    @DisplayName("Should return 2 users")
    void findAllWithSuccess() {
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

    @Test
    @DisplayName("Should return null")
    void findAllWithNoValues() {
        // Arrange
        when(repository.findAll()).thenReturn(null);

        // Act
        List<User> usersTest = service.findAll();

        // Assert
        verify(repository, times(1)).findAll();
        assertNull(usersTest);
    }

    @Test
    @DisplayName("Should return the only created user by ID")
    void findByIdWithSuccess() {
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
    @DisplayName("Should insert the only created user")
    void insertWithSuccess() {
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
    @DisplayName("Should delete the only user")
    void deleteWithSuccess() {
        //Arrange
        User user = new User(1L, "Maria Brown", "maria@gmail.com", "988888888", "123456");
        List<User> empty = new ArrayList<>();

        // Act
        repository.delete(user);

        // Assert
        assertEquals(service.findAll(), empty);
    }

    @Test
    @DisplayName("Should throw a ResourceNotFoundException when deleting inexistent user")
    void deleteInexistentUser() {
        // Arrange
        doThrow(ResourceNotFoundException.class).when(repository).deleteById(1L);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> service.delete(1L));
    }

    @Test
    @DisplayName("Should update the only stubbed user")
    void updateWithSuccess() {
        // Arrange
        User userToUpdate = new User(1L, "Maria Brown", "maria@gmail.com", "988888888", "123456");
        User updatedUser = new User(1L, "Maria Green", "maria@gmail.com", "988888888", "123456");

        when(repository.getReferenceById(userToUpdate.getId())).thenReturn(userToUpdate);
        when(repository.save(userToUpdate)).thenReturn(updatedUser);

        // Act
        User userTest = service.update(1L, updatedUser);

        // Assert
        verify(repository, times(1)).getReferenceById(1L);
        verify(repository, times(1)).save(updatedUser);
        assertEquals(updatedUser, userTest);
    }

    @Test
    @DisplayName("Should return NullPointerException")
    void updatePassingNull() {
        // Arrange
        User userToUpdate = new User(1L, "Maria Brown", "maria@gmail.com", "988888888", "123456");
        User updatedUser = null;

        when(repository.getReferenceById(userToUpdate.getId())).thenReturn(userToUpdate);
        when(repository.save(userToUpdate)).thenReturn(updatedUser);

        // Act;
        // Assert
        assertThrowsExactly(NullPointerException.class, () -> {
            User userTest = service.update(1L, updatedUser);
        });
    }
}