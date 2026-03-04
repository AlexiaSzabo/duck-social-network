package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.domain.Person;
import org.example.domain.User;
import org.example.service.PersonService;
import org.example.service.UserService;

import java.time.LocalDate;

public class AddPersonController {

    // USER fields
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    // PERSON fields
    @FXML private TextField lastNameField;
    @FXML private TextField firstNameField;
    @FXML private TextField occupationField;
    @FXML private TextField empathyField;
    @FXML private DatePicker birthDatePicker;

    private UserService userService;
    private PersonService personService;

    private Runnable callback; // refresh persons table

    public void setServices(UserService us, PersonService ps, Runnable callback) {
        this.userService = us;
        this.personService = ps;
        this.callback = callback;
    }

    @FXML
    private void handleAddPerson() {
        try {
            // USER part
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();

            // PERSON part
            String lastName = lastNameField.getText();
            String firstName = firstNameField.getText();
            String occupation = occupationField.getText();
            double empathy = Double.parseDouble(empathyField.getText());
            LocalDate birthDate = birthDatePicker.getValue();

            if (birthDate == null) {
                new Alert(Alert.AlertType.WARNING, "Select a valid birth date!").show();
                return;
            }

            // Create User
            User user = new User(username, email, password);
            user = userService.addUser(user);

            // Create Person
            Person p = new Person(
                    user.getId(),
                    lastName,
                    firstName,
                    occupation,
                    empathy,
                    birthDate
            );

            personService.addPerson(p);

            // Refresh main table
            if (callback != null)
                callback.run();

            closeWindow();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Empathy must be a number!").show();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}
