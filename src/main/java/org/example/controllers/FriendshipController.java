package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.example.domain.Friendship;
import org.example.service.FriendshipService;

import java.util.List;

public class FriendshipController {

    @FXML private TableView<Friendship> friendTable;
    @FXML private TableColumn<Friendship, Integer> user1Col;
    @FXML private TableColumn<Friendship, Integer> user2Col;

    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Label pageLabel;

    private FriendshipService friendshipService;

    private int currentPage = 1;
    private final int pageSize = 10;
    private long totalCount = 0;

    public void setServices(FriendshipService fs) {
        this.friendshipService = fs;

        initTable();
        friendTable.setItems(friendshipService.getObservableFriendships());
    }

    private void initTable() {
        user1Col.setCellValueFactory(new PropertyValueFactory<>("user1Id"));
        user2Col.setCellValueFactory(new PropertyValueFactory<>("user2Id"));
    }

    private void loadPage() {
        List<Friendship> friendships = friendshipService.getFriendshipPage(currentPage, pageSize);
        friendTable.setItems(FXCollections.observableArrayList(friendships));

        totalCount = friendshipService.getFriendshipCount();
        long totalPages = (long) Math.ceil((double) totalCount / pageSize);

        pageLabel.setText("Page " + currentPage + " / " + totalPages);

        prevButton.setDisable(currentPage == 1);
        nextButton.setDisable(currentPage >= totalPages);
    }

    @FXML
    private void handleNext() {
        currentPage++;
        loadPage();
    }

    @FXML
    private void handlePrev() {
        if (currentPage > 1) {
            currentPage--;
            loadPage();
        }
    }

    @FXML
    private void handleAddFriendship() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AddFriendshipView.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            AddFriendshipController ctrl = loader.getController();
            ctrl.setServices(friendshipService, this::loadPage);

            stage.setTitle("Add Friendship");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Cannot open AddFriendship window").show();
        }
    }

    @FXML
    private void handleRemoveFriendship() {
        Friendship fr = friendTable.getSelectionModel().getSelectedItem();

        if (fr == null) {
            new Alert(Alert.AlertType.WARNING, "Select a friendship first!").show();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete this friendship?",
                ButtonType.YES, ButtonType.NO);

        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            friendshipService.removeFriendship(fr.getUser1Id(), fr.getUser2Id());
            loadPage();
        }
    }

    @FXML
    private void handleCommunities() {
        int count = friendshipService.numberOfCommunities();
        new Alert(Alert.AlertType.INFORMATION,
                "Number of communities: " + count).show();
    }

    @FXML
    private void handleMostSociable() {
        var list = friendshipService.mostSociableCommunity();

        new Alert(Alert.AlertType.INFORMATION,
                "Most sociable community users: " + list).show();
    }
}
