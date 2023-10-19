package com.educandoweb.course.resources;

import com.educandoweb.course.entities.User;
import com.educandoweb.course.repositories.UserRepository;
import com.educandoweb.course.services.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.devtools.SeleniumCdpConnection;
import org.openqa.selenium.manager.SeleniumManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserResourceTest {
    @Autowired
    private UserRepository repository;
    @Autowired
    private UserResource resource;

    @Test
    @DisplayName("Should return 3 inserted users")
    void findAllWithInsertedUsers() {
        // Arrange
        var user1 = new User(null, "Maria Brown", "maria@gmail.com", "999999999", "123456");
        var user2 = new User(null, "James Greer", "jim@gmail.com", "999999998", "12345");
        var user3 = new User(null, "Jack Ryan", "jack@gmail.com", "999999987", "654321");

        List<User> users = new ArrayList<>(Arrays.asList(user1, user2, user3));

        for (User user: users) {
            resource.insert(user);
        }

        // Act
        var foundUsers = resource.findAll();

        // Assert
        assertEquals(foundUsers.getBody(), users);

        repository.deleteAll();
    }

    @Test
    @DisplayName("Should return user with matching ID")
    void findById() {
        // Arrange
        var user1 = new User(null, "Maria Brown", "maria@gmail.com", "999999999", "123456");
        var user2 = new User(null, "James Greer", "jim@gmail.com", "999999998", "12345");
        var user3 = new User(null, "Jack Ryan", "jack@gmail.com", "999999987", "654321");

        List<User> users = new ArrayList<>(Arrays.asList(user1, user2, user3));

        for (User user: users) {
            resource.insert(user);
        }

        // Act
        var foundUsers = resource.findById(user2.getId());

        // Assert
        assertEquals(Objects.requireNonNull(foundUsers.getBody()), user2);
        repository.deleteAll();
    }

    @Test
    void insert() {
        // Arrange
        var user1 = new User(null, "James Greer", "jim@gmail.com", "999999998", "12345");

        resource.insert(user1);

        // Act
        var foundUsers = resource.findById(user1.getId());

        // Assert
        assertEquals(foundUsers.getBody(), user1);
        repository.deleteAll();
    }

    @Test
    void delete() {
        // Arrange
        var user1 = new User(null, "Maria Brown", "maria@gmail.com", "999999999", "123456");
        var user2 = new User(null, "James Greer", "jim@gmail.com", "999999998", "12345");
        var user3 = new User(null, "Jack Ryan", "jack@gmail.com", "999999987", "654321");

        List<User> users = new ArrayList<>(Arrays.asList(user1, user2, user3));

        for (User user: users) {
            resource.insert(user);
        }

        // Act
        resource.delete(user2.getId());
        var foundUsers = resource.findAll();
        users.remove(user2);

        // Assert
        assertEquals(foundUsers.getBody(), users);
        repository.deleteAll();
    }

    @Test
    @Transactional
    void update() {
        repository.deleteAll();
        // Arrange
        var user1 = new User(null, "James Greer", "jim@gmail.com", "999999998", "12345");
        var user1_2 = new User(null, "James Brown", "james@gmail.com", "999999998", "12345");

        resource.insert(user1);

        // Act
        resource.update(user1.getId(), user1_2);
        var foundUsers = resource.findById(user1.getId());

        // Assert
        user1_2.setId(user1.getId());
        assertEquals(foundUsers.getBody(), user1_2);
        repository.deleteAll();
    }
}