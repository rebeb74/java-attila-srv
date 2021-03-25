package ch.codeattila.api.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.codeattila.api.api.models.User;
import ch.codeattila.api.api.services.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    // get all users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return this.userService.getAllUsers();
    }

    // get user by id
    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable(value = "id") long userId) {
        return this.userService.getUserById(userId);
    }

    // update user
    @PutMapping("/user/{id}")
    public User updateUser(@RequestBody User user, @PathVariable("id") long userId) {
        return this.userService.updateUser(user, userId);
    }

    // delete user by id
    @DeleteMapping("/user/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") long userId) {
        return this.userService.deleteUser(userId);
    }

}
