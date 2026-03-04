package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controllers.LoginController;
import org.example.database.DatabaseConnection;
import org.example.repository.*;
import org.example.service.*;
import org.example.validation.*;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // REPO AND VALIDATOR
        UserRepository userRepo = new UserRepository();
        DuckRepository duckRepo = new DuckRepository();
        PersonRepository personRepo = new PersonRepository();
        FriendshipRepository friendshipRepo = new FriendshipRepository();
        MessageRepository messageRepository = new MessageRepository(DatabaseConnection.getConnection());
        FriendRequestRepository friendRequestRepo = new FriendRequestRepository(DatabaseConnection.getConnection());

        var userValidator = new UserValidator();
        var duckValidator = new DuckValidator();
        var personValidator = new PersonValidator();

        // SERVICES
        UserService userService = new UserService(userRepo, userValidator);
        DuckService duckService = new DuckService(duckRepo, userRepo, duckValidator);
        PersonService personService = new PersonService(personRepo, userRepo, personValidator);
        FriendshipService friendshipService = new FriendshipService(friendshipRepo, userRepo);
        MessageService messageService = new MessageService(messageRepository);
        FriendRequestService friendRequestService = new FriendRequestService(friendRequestRepo, friendshipService);

        userService.migratePasswordsToHash();

        // LOGIN WINDOW
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
        Scene scene = new Scene(loader.load());

        LoginController ctrl = loader.getController();
        ctrl.setServices(userService, duckService, personService, friendshipService, messageService, friendRequestService);

        stage.setScene(scene);
        stage.setTitle("Login - Duck Social Network");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
