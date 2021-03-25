package ch.codeattila.api.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ch.codeattila.api.api.exception.ResourceNotFoundException;
import ch.codeattila.api.api.models.User;
import ch.codeattila.api.api.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // get all users
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    // get user by id
    public User getUserById(long userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id :" + userId));
    }

    // create user
    public User createUser(User user) {
        return this.userRepository.save(user);
    }

    // update user
    public User updateUser(User user, long userId) {
        User existingUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id :" + userId));
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setBirthdate(user.getBirthdate());
        existingUser.setFriends(user.getFriends());
        existingUser.setLang(user.getLang());
        existingUser.setUpdatedOn(user.getUpdatedOn());
        return this.userRepository.save(existingUser);
    }

    // delete user by id
    public ResponseEntity<User> deleteUser(long userId) {
        User existingUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id :" + userId));
        this.userRepository.delete(existingUser);
        return ResponseEntity.ok().build();
    }
}
