package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.domain.Duck;
import org.example.domain.TipRata;
import org.example.domain.User;
import org.example.service.DuckService;
import org.example.service.UserService;

public class AddDuckController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @FXML private ComboBox<TipRata> typeBox;

    @FXML private TextField speedField;
    @FXML private TextField staminaField;

    private UserService userService;
    private DuckService duckService;

    private Runnable callback; // reload tabel dupa add

    public void setServices(UserService us, DuckService ds, Runnable callback) {
        this.userService = us;
        this.duckService = ds;
        this.callback = callback;

        typeBox.getItems().setAll(TipRata.values());
    }

    @FXML
    private void handleAddDuck() {
        try {
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();

            TipRata tip = typeBox.getValue();
            double speed = Double.parseDouble(speedField.getText());
            double stamina = Double.parseDouble(staminaField.getText());

            if (tip == null) {
                new Alert(Alert.AlertType.WARNING,
                        "Please select a duck type.").show();
                return;
            }

            // create user first
            User user = new User(username, email, password);
            user = userService.addUser(user);

            // create duck using FULL constructor (7 arguments!)
            Duck duck = new Duck(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPassword(),
                    tip,
                    speed,
                    stamina
            );

            duckService.addDuck(duck);

            if (callback != null)
                callback.run();

            close();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR,
                    "Speed/Stamina must be numbers!").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,
                    "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    private void handleCancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}
