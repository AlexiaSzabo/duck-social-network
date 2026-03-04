package org.example.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;

import org.example.domain.FriendRequest;
import org.example.domain.User;
import org.example.service.FriendRequestService;
import org.example.service.FriendshipService;
import org.example.service.UserService;

public class FriendRequestController {

    @FXML private TableView<FriendRequest> requestsTable;
    @FXML private TableColumn<FriendRequest, String> fromColumn;
    @FXML private TableColumn<FriendRequest, String> statusColumn;
    @FXML private Button approveButton;
    @FXML private Button rejectButton;
    @FXML private Button closeButton;

    private FriendRequestService friendRequestService;
    private UserService userService;
    private FriendshipService friendshipService;
    private User loggedUser;

    private ObservableList<FriendRequest> pendingRequests;

    /**
     * Set services and logged user
     */
    public void setServices(FriendRequestService frs, UserService us, FriendshipService fs, User loggedUser) {
        this.friendRequestService = frs;
        this.userService = us;
        this.friendshipService = fs;
        this.loggedUser = loggedUser;

        initTable();

        //lista obsevable de la service
        pendingRequests = friendRequestService.getPendingRequestsObservable(loggedUser.getId());
        requestsTable.setItems(pendingRequests);
    }

    /**
     * Initialize TableView columns
     */
    private void initTable() {
        fromColumn.setCellValueFactory(cell -> {
            try {
                User from = userService.getUser(cell.getValue().getFromUserId());
                return new SimpleStringProperty(from.getUsername());
            } catch (Exception e) {
                return new SimpleStringProperty("Unknown");
            }
        });

        statusColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getStatus().name()));
    }

    /**
     * Approve selected request
     */
    @FXML
    private void handleApprove() {
        FriendRequest selected = requestsTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        try {
            // Update status in db
            friendRequestService.respondToRequest(selected.getId(), FriendRequest.Status.APPROVED);

            friendshipService.addFriendship(selected.getFromUserId(), selected.getToUserId());

            pendingRequests.remove(selected);

            String fromUsername;
            try {
                fromUsername = userService.getUser(selected.getFromUserId()).getUsername();
            } catch (Exception e) {
                fromUsername = "Unknown";
            }

            new Alert(Alert.AlertType.INFORMATION,
                    "Friendship with " + fromUsername + " was APPROVED!")
                    .show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Error approving friend request: " + e.getMessage()).show();
        }
    }

    /**
     * Reject selected request
     */
    @FXML
    private void handleReject() {
        FriendRequest selected = requestsTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        try {
            // Update status in db
            friendRequestService.respondToRequest(selected.getId(), FriendRequest.Status.REJECTED);

            pendingRequests.remove(selected);
            String fromUsername;
            try {
                fromUsername = userService.getUser(selected.getFromUserId()).getUsername();
            } catch (Exception e) {
                fromUsername = "Unknown";
            }

            new Alert(Alert.AlertType.INFORMATION,
                    "Friendship request from " + fromUsername + " was REJECTED!")
                    .show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Error rejecting friend request: " + e.getMessage()).show();
        }
    }

    /**
     * Close window
     */
    @FXML
    private void handleClose() {
        Stage stage = (Stage) requestsTable.getScene().getWindow();
        stage.close();
    }
}
