package org.example.domain;

/**
 * Represents a user stored in the "users" database table.
 * Each Person or Duck is linked to one User through a foreign key.
 */
public class User extends Entity {

    private String username;
    private String email;
    private String password;

    public User(Integer id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String username, String email, String password) {
        this(null, username, email, password);
    }

    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public User(Integer id) {
        this.id = id;
        this.username = null;
        this.email = null;
        this.password = null;
    }


    @Override
    public String toString() {
        return String.format("User{id=%d, username='%s', email='%s'}",
                id, username, email);
    }

}
