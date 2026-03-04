package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.service.FriendshipService;
import org.example.service.UserService;

public class AddFriendshipController {

    @FXML private TextField user1Field;
    @FXML private TextField user2Field;

    private FriendshipService friendshipService;
    private Runnable callback;

    public void setServices(FriendshipService friendshipService, Runnable callback) {
        this.friendshipService = friendshipService;
        this.callback = callback;
    }

    @FXML
    private void handleAdd() {
        try {
            int u1 = Integer.parseInt(user1Field.getText());
            int u2 = Integer.parseInt(user2Field.getText());

            if (u1 == u2) {
                new Alert(Alert.AlertType.WARNING,
                        "A user cannot befriend themselves!").show();
                return;
            }

            // Add friendship
            friendshipService.addFriendship(u1, u2);

            if (callback != null)
                callback.run(); // refresh table

            close();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR,
                    "User IDs must be numeric!").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,
                    "Error adding friendship: " + e.getMessage()).show();
        }
    }

    @FXML
    private void handleCancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) user1Field.getScene().getWindow();
        stage.close();
    }
}
