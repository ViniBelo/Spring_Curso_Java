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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    @DisplayName("Should return nothing")
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
    @DisplayName("Should return the only stubbed user")
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
    @DisplayName("Should return null")
    void findByIdWithNoValues() {
        // Arrange
        Optional<User> u1 = Optional.empty();

        // Act
        when(repository.findById(null)).thenReturn(null);
        Optional<User> u2 = this.repository.findById(null);

        // Assert
        verify(repository, times(1)).findById(null);
        assertThrows(NullPointerException.class, () -> service.findById(null));
    }


    @Test
    @DisplayName("Should insert the only stubbed user")
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
    @DisplayName("Should insert nothing")
    void insertWithNoValues() {
        // Arrange
        User user = null;

        when(repository.save(user)).thenReturn(user);

        // Act
        User savedUser = service.insert(user);

        // Assert
        verify(repository, times(1)).save(user);
        assertEquals(user, savedUser);
    }

    @Test
    @DisplayName("Should delete the only stubbed user")
    void deleteWithSuccess() {
        //Arrange
        User user = new User(1L, "Maria Brown", "maria@gmail.com", "988888888", "123456");

        // Act
        service.delete(user.getId());

        // Assert
        verify(repository, times(1)).deleteById(user.getId());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
    }

    @Test
    @DisplayName("Should delete the only stubbed user")
    void deleteWithNoValues() throws NullPointerException{
        //Arrange
        User user = null;

        // Act
        Exception exception = assertThrows(NullPointerException.class, () -> service.delete(null));

        // Assert
        assertEquals(NullPointerException.class.getName(), exception.getClass().getName());
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
    @DisplayName("Should return null exception")
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