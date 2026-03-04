package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.example.domain.Person;
import org.example.service.PersonService;
import org.example.service.UserService;

import java.util.List;

public class PersonsController {

    @FXML private TableView<Person> personsTable;
    @FXML private TableColumn<Person, Integer> idCol;
    @FXML private TableColumn<Person, String> usernameCol;
    @FXML private TableColumn<Person, String> emailCol;
    @FXML private TableColumn<Person, String> lastNameCol;
    @FXML private TableColumn<Person, String> firstNameCol;
    @FXML private TableColumn<Person, String> occupationCol;
    @FXML private TableColumn<Person, Double> empathyCol;

    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Label pageLabel;

    private UserService userService;
    private PersonService personService;

    private int currentPage = 1;
    private final int pageSize = 10;
    private long totalCount = 0;

    public void setServices(UserService us, PersonService ps) {
        this.userService = us;
        this.personService = ps;

        initTable();
        loadPage();
    }

    private void initTable() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("nume"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("prenume"));
        occupationCol.setCellValueFactory(new PropertyValueFactory<>("ocupatie"));
        empathyCol.setCellValueFactory(new PropertyValueFactory<>("nivelEmpatie"));
    }

    private void loadPage() {
        List<Person> persons = personService.getPersonPage(currentPage, pageSize);
        personsTable.setItems(FXCollections.observableArrayList(persons));

        totalCount = personService.getPersonCount();
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

    // ------------------------------
    // ADD PERSON WINDOW
    // ------------------------------
    @FXML
    private void handleAddPerson() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AddPersonView.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            AddPersonController ctrl = loader.getController();
            ctrl.setServices(userService, personService, this::loadPage);

            stage.setTitle("Add Person");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Cannot open Add Person window").show();
        }
    }

    // ------------------------------
    // REMOVE PERSON
    // ------------------------------
    @FXML
    private void handleRemovePerson() {
        Person p = personsTable.getSelectionModel().getSelectedItem();

        if (p == null) {
            new Alert(Alert.AlertType.WARNING, "Select a person first!").show();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete this person?",
                ButtonType.YES, ButtonType.NO);

        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            // delete person + user
            personService.deletePerson(p.getId());
            userService.deleteUser(p.getId());

            loadPage();
        }
    }
}
