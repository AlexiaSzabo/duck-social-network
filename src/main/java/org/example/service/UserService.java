package org.example.service;

import org.example.domain.User;
import org.example.exception.NotFoundException;
import org.example.repository.UserRepository;
import org.example.util.PasswordUtils;
import org.example.validation.ValidationStrategy;

import java.util.List;

public class UserService {

    private final UserRepository repo;
    private final ValidationStrategy<User> validator;

    public UserService(UserRepository repo, ValidationStrategy<User> validator) {
        this.repo = repo;
        this.validator = validator;
    }

    /**
     * Adds a user and makes the password hashed
     */
    public User addUser(User user) {
        validator.validate(user);

        // hash password for the new users
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String hashed = PasswordUtils.hash(user.getPassword());
            user.setPassword(hashed);
        }

        return repo.save(user);
    }

    /**
     * Returns a user or throws NotFoundException.
     */
    public User getUser(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id));
    }

    /**
     * deletes a user by id
     */
    public void deleteUser(Integer id) {
        repo.delete(id);
    }

    /**
     * Verifies if the plain-text password is the same as the hashed one in db
     */
    public boolean checkPassword(User user, String plainPassword) {
        String dbPassword = user.getPassword();

        if (dbPassword == null) return false;

        // if the password is hashed
        if (dbPassword.length() == 64) {
            return PasswordUtils.verify(plainPassword, dbPassword);
        } else {
            // old password not-hashed
            return dbPassword.equals(plainPassword);
        }
    }

    /**
     * Returns all users
     */
    public List<User> getAllUsers() {
        return repo.findAll();
    }

    /**
     * Returns the number of the users
     */
    public long countUsers() {
        return repo.count();
    }


    /**
     * changes all the passwords to hash
     */
    public void migratePasswordsToHash() {
        List<User> allUsers = getAllUsers();

        for (User user : allUsers) {
            String plainPassword = user.getPassword();

            if (plainPassword != null && plainPassword.length() != 64) {
                String hashedPassword = PasswordUtils.hash(plainPassword);
                user.setPassword(hashedPassword);
                repo.update(user);
                System.out.println("Migrated password for user: " + user.getUsername());
            }
        }

        System.out.println("Password migration completed!");
    }
}
