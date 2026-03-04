package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.example.domain.Duck;
import org.example.domain.TipRata;
import org.example.service.DuckService;
import org.example.service.UserService;

import java.util.List;

public class DuckController {

    private UserService userService;
    private DuckService duckService;

    // Pagination
    private int currentPage = 1;
    private final int pageSize = 10;
    private long totalDucks = 0;

    @FXML private Label welcomeLabel;

    @FXML private TableView<Duck> duckTableView;

    @FXML private TableColumn<Duck, Integer> idColumn;
    @FXML private TableColumn<Duck, String> usernameColumn;
    @FXML private TableColumn<Duck, String> emailColumn;
    @FXML private TableColumn<Duck, TipRata> typeColumn;
    @FXML private TableColumn<Duck, Integer> cardIdColumn;
    @FXML private TableColumn<Duck, Double> staminaColumn;
    @FXML private TableColumn<Duck, Double> speedColumn;

    @FXML private ComboBox<TipRata> typeComboBox;

    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Label pageLabel;

    @FXML private Button addDuckButton;
    @FXML private Button removeDuckButton;

    public void setServices(UserService us, DuckService ds) {
        this.userService = us;
        this.duckService = ds;

        initTable();
        initFilter();
        loadPage();
    }

    // ----------------------------
    // TABLE
    // ----------------------------
    private void initTable() {
        welcomeLabel.setText("DUCK SOCIAL NETWORK");

        duckTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("tip"));
        cardIdColumn.setCellValueFactory(new PropertyValueFactory<>("cardId"));
        staminaColumn.setCellValueFactory(new PropertyValueFactory<>("rezistenta"));
        speedColumn.setCellValueFactory(new PropertyValueFactory<>("viteza"));
    }

    // ----------------------------
    // FILTER (ENUM + ALL)
    // ----------------------------
    private void initFilter() {
        typeComboBox.getItems().add(null); // ALL
        typeComboBox.getItems().addAll(TipRata.values());
        typeComboBox.setPromptText("ALL TYPES");

        typeComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(TipRata item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setText(null);
                else setText(item == null ? "ALL TYPES" : item.name());
            }
        });

        typeComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(TipRata item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "ALL TYPES" : item.name());
            }
        });

        typeComboBox.valueProperty().addListener((obs, o, n) -> {
            currentPage = 1;
            loadPage();
        });
    }

    // ----------------------------
    // LOAD PAGE (DB does EVERYTHING)
    // ----------------------------
    private void loadPage() {
        TipRata filter = typeComboBox.getValue();

        List<Duck> ducks =
                duckService.getDuckPage(currentPage, pageSize, filter);

        totalDucks =
                duckService.getDuckCount(filter);

        duckTableView.setItems(FXCollections.observableArrayList(ducks));
        updatePageLabel();
    }

    private void updatePageLabel() {
        long totalPages = (long) Math.ceil((double) totalDucks / pageSize);
        if (totalPages == 0) totalPages = 1;

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

    // ----------------------------
    // ADD DUCK
    // ----------------------------
    @FXML
    private void handleAddDuck() {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/views/AddDuckView.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            AddDuckController ctrl = loader.getController();
            ctrl.setServices(userService, duckService, this::loadPage);

            stage.setTitle("Add Duck");
            stage.show();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,
                    "Could not open Add Duck window").show();
        }
    }

    // ----------------------------
    // REMOVE DUCK
    // ----------------------------
    @FXML
    private void handleRemoveDuck() {
        Duck selected = duckTableView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Select a duck first!").show();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure?",
                ButtonType.YES, ButtonType.NO);

        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            duckService.deleteDuck(selected.getId());
            loadPage();
        }
    }
}
