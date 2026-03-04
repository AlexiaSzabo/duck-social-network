package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.example.domain.User;
import org.example.service.MessageService;
import org.example.service.UserService;

public class ChatSelectController {

    private UserService userService;
    private MessageService messageService;
    private User loggedUser;
    private MainController mainController;

    @FXML private ListView<String> userListView;

    // Setam serviciile si utilizatorul logat
    public void setServices(UserService userService, MessageService messageService, User loggedUser) {
        this.userService = userService;
        this.messageService = messageService;
        this.loggedUser = loggedUser;
        loadUsers();
    }

    // Setam referința la MainController
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private void loadUsers() {
        userListView.getItems().clear();
        for (User u : userService.getAllUsers()) {
            if (!u.equals(loggedUser)) {
                userListView.getItems().add(u.getUsername());
            }
        }
    }

    @FXML
    private void openChatWithSelectedUser() {
        String selectedUsername = userListView.getSelectionModel().getSelectedItem();
        if (selectedUsername == null) return;

        User selectedUser = userService.getAllUsers().stream()
                .filter(u -> u.getUsername().equals(selectedUsername))
                .findFirst()
                .orElse(null);

        if (selectedUser == null) return;

        if (mainController != null) {
            mainController.openDualChat(loggedUser, selectedUser);
        } else {
            System.out.println("MainController not set!");
        }
    }
}
