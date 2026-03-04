package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.domain.User;
import org.example.service.*;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private UserService userService;
    private DuckService duckService;
    private PersonService personService;
    private FriendshipService friendshipService;
    private MessageService messageService;
    private FriendRequestService friendRequestService;

    public void setServices(UserService us,
                            DuckService ds,
                            PersonService ps,
                            FriendshipService fs,
                            MessageService ms,
                            FriendRequestService frs) {
        this.userService = us;
        this.duckService = ds;
        this.personService = ps;
        this.friendshipService = fs;
        this.messageService = ms;
        this.friendRequestService = frs;
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password.");
            return;
        }

        try {
            User user = userService.getAllUsers().stream()
                    .filter(u -> u.getUsername().equals(username))
                    .findFirst()
                    .orElseThrow(() -> new Exception("User not found"));

            if (!userService.checkPassword(user, password)) {
                errorLabel.setText("Incorrect password or username !");
                return;
            }

            errorLabel.setText("");
            openMainWindow(user);

        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
        }
    }

    private void openMainWindow(User loggedUser) throws Exception {
        try {
            System.out.println("Opening MainWindow...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainWindow.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            System.out.println("FXML loaded successfully!");

            MainController ctrl = loader.getController();
            ctrl.setServices(userService, duckService, personService, friendshipService, messageService,friendRequestService, loggedUser);

            stage.setTitle("Duck Social Network");
            stage.show();

            // Închide fereastra de login
            Stage loginStage = (Stage) usernameField.getScene().getWindow();
            loginStage.close();
        } catch (Exception e) {
            e.printStackTrace();  // aici vei vedea exact ce lipsește
            new Alert(Alert.AlertType.ERROR, "Cannot open Main window: " + e.getMessage()).show();
        }
    }


    @FXML
    private void handleCancel() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}
