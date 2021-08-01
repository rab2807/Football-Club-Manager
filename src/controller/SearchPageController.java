package controller;

import database.*;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import database.PlayerOnSale;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

enum SearchMode {
    NONE, NAME, COUNTRY, POSITION, SALARY
}

public class SearchPageController implements Initializable {

    private Main main;
    private Club club;
    private SearchMode mode = SearchMode.NONE;
    private AnimationTimer at;

    @FXML
    private Button homeButton, buyButton, maxButton, logoutButton, submitButton, searchButton, sellButton, detailsButton, yesButton, noButton;
    @FXML
    private TableView<Player> table;
    @FXML
    private TableColumn<Player, String> nameCol, positionCol, countryCol;
    @FXML
    private TableColumn<Player, Integer> numberCol, ageCol;
    @FXML
    private TableColumn<Player, Integer> salaryCol, heightCol;
    @FXML
    private ComboBox<String> dropDown;
    @FXML
    private TextField searchBar;
    @FXML
    private Pane searchPane, canvasPane, alertPane;
    @FXML
    private AnchorPane lowerPane, upperPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Canvas canvas;
    @FXML
    private HBox hBox;
    @FXML
    private Label clubNameLabel;

    @FXML
    void onKeyPressedSearch(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
            submit();
    }

    @FXML
    void onMouseClickedSubmit(MouseEvent event) {
        submit();
    }

    @FXML
    void onMouseClickedBuy(MouseEvent event) {
        UIUtil.fadeOutAll(600, scrollPane, canvasPane, searchPane, hBox);
        TranslateTransition tt = UIUtil.move(600, lowerPane.getTranslateX(), lowerPane.getTranslateY(), -220, lowerPane.getTranslateY(), lowerPane);
        UIUtil.move(400, upperPane.getTranslateX(), upperPane.getTranslateY(), -50, upperPane.getTranslateY(), upperPane).play();
        tt.setOnFinished(ActionEvent -> {
            try {
                at.stop();
                main.showBuyPage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        tt.play();
    }

    @FXML
    void onMouseClickedHome(MouseEvent event) {
        UIUtil.fadeOutAll(600, scrollPane, canvasPane, searchPane, hBox);
        TranslateTransition tt = UIUtil.move(600, lowerPane.getTranslateX(), lowerPane.getTranslateY(), -220, lowerPane.getTranslateY(), lowerPane);
        UIUtil.move(400, upperPane.getTranslateX(), upperPane.getTranslateY(), -50, upperPane.getTranslateY(), upperPane).play();
        tt.setOnFinished(ActionEvent -> {
            try {
                at.stop();
                main.showHomePage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        tt.play();
    }

    @FXML
    void onMouseClickedLogout(MouseEvent event) {
        UIUtil.fadeOutAll(600, scrollPane, canvasPane, searchPane, hBox);
        TranslateTransition tt = UIUtil.move(600, lowerPane.getTranslateX(), lowerPane.getTranslateY(), -220, lowerPane.getTranslateY(), lowerPane);
        UIUtil.move(400, upperPane.getTranslateX(), upperPane.getTranslateY(), -50, upperPane.getTranslateY(), upperPane).play();
        tt.setOnFinished(ActionEvent -> {
            try {
                at.stop();
                main.showLoginPage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        tt.play();
    }

    @FXML
    void onMouseClickedMax(MouseEvent event) {
        UIUtil.fadeOutAll(600, scrollPane, canvasPane, searchPane, hBox);
        TranslateTransition tt = UIUtil.move(600, lowerPane.getTranslateX(), lowerPane.getTranslateY(), -220, lowerPane.getTranslateY(), lowerPane);
        UIUtil.move(400, upperPane.getTranslateX(), upperPane.getTranslateY(), -50, upperPane.getTranslateY(), upperPane).play();
        tt.setOnFinished(ActionEvent -> {
            try {
                at.stop();
                main.showStatsPage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        tt.play();
    }

    @FXML
    void onMouseClickedSell(MouseEvent event) {
        alertOpen();
    }

    @FXML
    void onMouseClickedDetails(MouseEvent event) {
        Player player = table.getSelectionModel().getSelectedItem();
        if (player != null) {
            try {
                main.showPlayerPage(player);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void onMouseClickedYes(MouseEvent event) {
        // Create a PlayerOnSale object of the selected Player object with a random price
        // send the PlayerOnSale object to server and remove player from club list
        Player player = table.getSelectionModel().getSelectedItem();
        if (player != null) {
            try {
                main.getSocket().write(new PlayerOnSale(player, (int) (player.getSalary() * (1 + new Random().nextDouble()))));
            } catch (IOException e) {
                e.printStackTrace();
            }
            table.getItems().remove(player);
            club.removePlayer(player);
        }
        alertClose();
    }

    @FXML
    void onMouseClickedNo(MouseEvent event) {
        alertClose();
    }

    private void submit() {
        if (submitAction()) {
            FadeTransition ft = UIUtil.fadeOut(300, scrollPane);
            ft.setOnFinished(ActionEvent -> {
                UIUtil.fadeInAll(300, scrollPane);
            });
            ft.play();
        }
    }

    private boolean submitAction() {
        table.getSelectionModel().clearSelection();
        if (mode == SearchMode.NONE) {
            UIUtil.shake(80, dropDown).play();
            return false;
        } else {
            String text = searchBar.getText();
            if (mode == SearchMode.NAME) {
                loadTable(PlayerSearch.searchByName(text, club.getList()));
            } else if (mode == SearchMode.COUNTRY) {
                loadTable(PlayerSearch.searchByCountry(text, club.getList()));
            } else if (mode == SearchMode.POSITION) {
                loadTable((PlayerSearch.searchByPosition(text, club.getList())));
            } else {
                String[] tokens = text.split("-");
                double lower, upper;
                try {
                    lower = Double.parseDouble(tokens[0]);
                    upper = Double.parseDouble(tokens[1]);
                    if (lower > upper)
                        throw new Exception();
                    loadTable(PlayerSearch.searchBySalary(lower, upper, club.getList()));
                } catch (Exception e) {
                    UIUtil.shake(80, searchBar).play();
                    return false;
                }
            }
        }
        return true;
    }


    @FXML
    void onMouseEnteredBuy(MouseEvent event) {
        UIUtil.mouseEnteredEffectDark(buyButton);
    }

    @FXML
    void onMouseEnteredHome(MouseEvent event) {
        UIUtil.mouseEnteredEffectDark(homeButton);
    }

    @FXML
    void onMouseEnteredLogout(MouseEvent event) {
        UIUtil.mouseEnteredEffectDark(logoutButton);
    }

    @FXML
    void onMouseEnteredMax(MouseEvent event) {
        UIUtil.mouseEnteredEffectDark(maxButton);
    }

    @FXML
    void onMouseEnteredSubmit(MouseEvent event) {
        UIUtil.mouseEnteredEffectLight(submitButton);
    }

    @FXML
    void onMouseEnteredSell(MouseEvent event) {
        UIUtil.mouseEnteredEffectSpecial(sellButton);
    }

    @FXML
    public void onMouseEnteredYes(MouseEvent mouseEvent) {
        UIUtil.mouseEnteredEffectLight(yesButton);
    }

    @FXML
    void onMouseEnteredDetails(MouseEvent event) {
        detailsButton.setStyle("-fx-background-color: #b9b6b6; -fx-text-fill:  black ; -fx-border-color: transparent;");
    }

    @FXML
    public void onMouseEnteredNo(MouseEvent mouseEvent) {
        UIUtil.mouseEnteredEffectLight(noButton);
    }


    @FXML
    void onMouseExitedBuy(MouseEvent event) {
        UIUtil.mouseExitedEffectDark(buyButton);
    }

    @FXML
    void onMouseExitedHome(MouseEvent event) {
        UIUtil.mouseExitedEffectDark(homeButton);
    }

    @FXML
    void onMouseExitedLogout(MouseEvent event) {
        UIUtil.mouseExitedEffectDark(logoutButton);
    }

    @FXML
    void onMouseExitedMax(MouseEvent event) {
        UIUtil.mouseExitedEffectDark(maxButton);
    }

    @FXML
    void onMouseExitedSubmit(MouseEvent event) {
        UIUtil.mouseExitedEffectLight(submitButton);
    }

    @FXML
    void onMouseExitedSell(MouseEvent event) {
        UIUtil.mouseExitedEffectSpecial(sellButton);
    }

    @FXML
    public void onMouseExitedYes(MouseEvent mouseEvent) {
        UIUtil.mouseExitedEffectLight(yesButton);
    }

    @FXML
    void onMouseExitedDetails(MouseEvent event) {
        detailsButton.setStyle("-fx-background-color: #dfdfdf; -fx-text-fill:  black ; -fx-border-color: transparent;");
    }

    @FXML
    public void onMouseExitedNo(MouseEvent mouseEvent) {
        UIUtil.mouseExitedEffectLight(noButton);
    }


    private void setDropDown() {
        ObservableList<String> list = FXCollections.observableArrayList("Name", "Country", "Position", "Salary");
        dropDown.setItems(list);
    }

    private void loadTable(List<Player> playerList) {
        ObservableList<Player> observableList = FXCollections.observableArrayList();
        observableList.addAll(playerList);

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        numberCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        positionCol.setCellValueFactory(new PropertyValueFactory<>("position"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        heightCol.setCellValueFactory(new PropertyValueFactory<>("height"));
        salaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));

        table.setItems(observableList);
    }

    private void alertOpen() {
        scrollPane.setEffect(new GaussianBlur());
        upperPane.setEffect(new GaussianBlur());
        lowerPane.setEffect(new GaussianBlur());
        canvasPane.setEffect(new GaussianBlur());
        searchPane.setEffect(new GaussianBlur());
        hBox.setEffect(new GaussianBlur());
        UIUtil.fadeInAll(500, alertPane);
        alertPane.setDisable(false);
    }

    private void alertClose() {
        scrollPane.setEffect(null);
        upperPane.setEffect(null);
        lowerPane.setEffect(null);
        canvasPane.setEffect(null);
        searchPane.setEffect(null);
        hBox.setEffect(null);
        UIUtil.fadeOutAll(300, alertPane);
        alertPane.setDisable(true);
    }

    void initialize() {
        this.club = main.getClub();
        setDropDown();
        clubNameLabel.setText(club.getName());

        dropDown.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    switch (newValue) {
                        case "Name" -> mode = SearchMode.NAME;
                        case "Country" -> mode = SearchMode.COUNTRY;
                        case "Position" -> mode = SearchMode.POSITION;
                        case "Salary" -> mode = SearchMode.SALARY;
                    }
                }
        );
    }

    void setMain(Main main) {
        this.main = main;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UIUtil.highlightButton(searchButton);
        UIUtil.fadeInAll(600, scrollPane, canvasPane, searchPane, hBox);
        TranslateTransition t1 = UIUtil.move(600, lowerPane.getTranslateX(), lowerPane.getTranslateY(), 0, lowerPane.getTranslateY(), lowerPane);
        TranslateTransition t2 = UIUtil.move(400, upperPane.getTranslateX(), upperPane.getTranslateY(), 0, upperPane.getTranslateY(), upperPane);
        new SequentialTransition(t1, t2).play();

        at = UIUtil.draw(canvas, 10);
        at.start();
    }
}
