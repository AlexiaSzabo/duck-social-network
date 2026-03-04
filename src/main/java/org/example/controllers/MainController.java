package org.example.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.domain.User;
import org.example.service.*;

public class MainController {

    private UserService userService;
    private DuckService duckService;
    private PersonService personService;
    private FriendshipService friendshipService;
    private MessageService messageService;
    private FriendRequestService friendRequestService;
    private User loggedUser;

    @FXML private Label notificationsLabel;
    @FXML private Button friendRequestsButton;
    @FXML private Label friendRequestsBadge;


    private Timeline notificationTimeline;

    // Setam serviciile si utilizatorul logat
    public void setServices(UserService userService,
                            DuckService duckService,
                            PersonService personService,
                            FriendshipService friendshipService,
                            MessageService messageService,
                            FriendRequestService friendRequestService,
                            User loggedUser) {
        this.userService = userService;
        this.duckService = duckService;
        this.personService = personService;
        this.friendshipService = friendshipService;
        this.messageService = messageService;
        this.friendRequestService = friendRequestService;
        this.loggedUser = loggedUser;

        //face refresh la notificari
        startNotificationRefresh();
    }


    //notification refresh
    private void startNotificationRefresh() {
        notificationTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> updateNotifications())
        );
        notificationTimeline.setCycleCount(Timeline.INDEFINITE);
        notificationTimeline.play();
    }

    private void updateNotifications() {
        // ia nr de cereri care sunt pending
        int pending = friendRequestService
                .getPendingRequestsForUser(loggedUser.getId())
                .size();

        //pune nr de requests pending in dreapta
        friendRequestsBadge.setText(String.valueOf(pending));


        //daca exista cerere il face rosu daca nu revine la normal
        if (pending > 0) {
            friendRequestsButton.setStyle(
                    "-fx-border-color: red; -fx-border-width: 2;"
            );

            friendRequestsBadge.setStyle(
                    "-fx-background-color: red;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-padding: 2 6 2 6;" +
                            "-fx-background-radius: 10;"
            );
        } else {
            friendRequestsButton.setStyle("");

            friendRequestsBadge.setStyle(
                    "-fx-background-color: lightgray;" +
                            "-fx-text-fill: black;" +
                            "-fx-font-weight: bold;" +
                            "-fx-padding: 2 6 2 6;" +
                            "-fx-background-radius: 10;"
            );
        }
    }



    @FXML
    private void openDucksWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/DuckView.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            DuckController ctrl = loader.getController();
            ctrl.setServices(userService, duckService);

            stage.setTitle("Ducks Menu");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Cannot open Ducks window").show();
        }
    }

    @FXML
    private void openPersonsWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/PersonsView.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            PersonsController ctrl = loader.getController();
            ctrl.setServices(userService, personService);

            stage.setTitle("Persons Menu");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Cannot open Persons window").show();
        }
    }

    @FXML
    private void openFriendshipsWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/FriendshipView.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            FriendshipController ctrl = loader.getController();
            ctrl.setServices(friendshipService);

            stage.setTitle("Friendships Menu");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Cannot open Friendship window").show();
        }
    }

    @FXML
    private void openChatSelectWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ChatSelectView.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            ChatSelectController ctrl = loader.getController();
            ctrl.setServices(userService, messageService, loggedUser); // 3 argumente
            ctrl.setMainController(this); // referința la MainController

            stage.setTitle("Select User to Chat");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Cannot open Chat selection window").show();
        }
    }

    //  Publica pentru a fi accesibila din ChatSelectController
    public void openDualChat(User user1, User user2) {
        try {
            // Fereastra 1
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/views/ChatView.fxml"));
            Stage stage1 = new Stage();
            stage1.setScene(new Scene(loader1.load()));
            ChatController ctrl1 = loader1.getController();
            ctrl1.setServices(messageService, user1, user2);
            stage1.setTitle(user1.getUsername() + "'s perspective");
            stage1.show();

            // Fereastra 2
            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/views/ChatView.fxml"));
            Stage stage2 = new Stage();
            stage2.setScene(new Scene(loader2.load()));
            ChatController ctrl2 = loader2.getController();
            ctrl2.setServices(messageService, user2, user1);
            stage2.setTitle(user2.getUsername() + "'s perspective");
            stage2.show();

            // Oprire refresh cand se inchid ferestrele
            stage1.setOnCloseRequest(e -> ctrl1.stopRefresh());
            stage2.setOnCloseRequest(e -> ctrl2.stopRefresh());

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Cannot open dual chat").show();
        }
    }


    @FXML
    private void openFriendRequests() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/FriendRequestView.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            FriendRequestController ctrl = loader.getController();
            ctrl.setServices(friendRequestService, userService, friendshipService,loggedUser);

            stage.setTitle("Friend Requests");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Cannot open Friend Requests").show();
        }
    }

    @FXML
    private void openSendFriendRequestWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/SendFriendRequestView.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            FriendRequestSendController ctrl = loader.getController();
            ctrl.setServices(userService, friendRequestService, friendshipService, loggedUser);

            stage.setTitle("Send Friend Request");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Cannot open Send Friend Request window").show();
        }
    }



    @FXML
    private void handleLogout() {
        if (notificationTimeline != null) notificationTimeline.stop();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            LoginController ctrl = loader.getController();
            ctrl.setServices(userService, duckService, personService, friendshipService, messageService,friendRequestService);
            stage.setTitle("Login");
            stage.show();

            // inchide fereastra curenta
            Stage mainStage = (Stage) notificationsLabel.getScene().getWindow();
            mainStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}
