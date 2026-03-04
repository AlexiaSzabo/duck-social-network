package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import org.example.domain.FriendRequest;
import org.example.domain.User;
import org.example.service.FriendRequestService;
import org.example.service.FriendshipService;
import org.example.service.UserService;

import java.util.List;

public class FriendRequestSendController {

    @FXML
    private ComboBox<User> userComboBox;

    @FXML
    private Label statusLabel;

    private UserService userService;
    private FriendRequestService friendRequestService;
    private FriendshipService friendshipService;
    private User loggedUser;

    // ObservableList pentru ComboBox
    private final ObservableList<User> usersObservable = FXCollections.observableArrayList();


    public void setServices(UserService userService,
                            FriendRequestService friendRequestService,
                            FriendshipService friendshipService,
                            User loggedUser) {
        this.userService = userService;
        this.friendRequestService = friendRequestService;
        this.friendshipService = friendshipService;
        this.loggedUser = loggedUser;

        loadUsers();
    }

    // incarcă userii disponibili pentru trimiterea request-ului
    private void loadUsers() {
        List<User> allUsers = userService.getAllUsers();

        // fara user-ul logat
        allUsers.removeIf(u -> u.getId().equals(loggedUser.getId()));

        // fara userii care sunt deja prieteni
        List<Integer> friendsIds = friendshipService.findFriendsOf(loggedUser.getId());
        allUsers.removeIf(u -> friendsIds.contains(u.getId()));

        // fara userii de la care inca astept acceptarea friend requestului
        List<Integer> pendingSentIds = friendRequestService.getAllRequests().stream()
                .filter(fr -> fr.getFromUserId().equals(loggedUser.getId())
                        && fr.getStatus() == FriendRequest.Status.PENDING)
                .map(FriendRequest::getToUserId)
                .toList();
        allUsers.removeIf(u -> pendingSentIds.contains(u.getId()));


        usersObservable.setAll(allUsers);
        userComboBox.setItems(usersObservable);
    }


    @FXML
    private void handleSendRequest() {
        User selected = userComboBox.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Please select a user.");
            return;
        }

        if (!friendRequestService.canSendRequest(loggedUser.getId(), selected.getId())) {
            statusLabel.setText("You cannot send a request to this user.");
            return;
        }

        try {
            // creeaza request si il adauga in tabel
            FriendRequest fr = new FriendRequest();
            fr.setFromUserId(loggedUser.getId());
            fr.setToUserId(selected.getId());
            fr.setStatus(FriendRequest.Status.PENDING);

            friendRequestService.addFriendRequest(fr);

            // afișeaza mesaj de succes
            statusLabel.setText("Friend request sent to " + selected.getUsername() + "!");

            // scoate userul din ComboBox dupa trimitere
            usersObservable.remove(selected);

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Failed to send friend request.");
        }
    }
}
