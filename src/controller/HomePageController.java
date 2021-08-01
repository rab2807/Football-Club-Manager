package controller;

import database.*;
import javafx.animation.AnimationTimer;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import messagetype.LoginMessageClient;
import database.PlayerOnSale;
import messagetype.SellMessageClient;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {

    private Main main;
    private Club club;
    private AnimationTimer at;

    @FXML
    private Button searchButton, buyButton, maxButton, logoutButton, homeButton, sellButton, detailsButton, yesButton, noButton;
    @FXML
    private TableView<Player> table;
    @FXML
    private TableColumn<Player, String> nameCol, positionCol, countryCol;
    @FXML
    private TableColumn<Player, Integer> numberCol, ageCol;
    @FXML
    private TableColumn<Player, Double> heightCol, salaryCol;
    @FXML
    private AnchorPane lowerPane, upperPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Pane canvasPane, alertPane;
    @FXML
    private Canvas canvas;
    @FXML
    private HBox hBox;
    @FXML
    private Label clubNameLabel;

    @FXML
    void onMouseClickedBuy(MouseEvent event) {
        UIUtil.fadeOutAll(600, canvasPane, scrollPane, hBox);
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
    void onMouseClickedLogout(MouseEvent event) {
        try {
            main.getSocket().write(new LoginMessageClient(club.getName(), false, main.getSocket()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        UIUtil.fadeOutAll(600, canvasPane, scrollPane, hBox);
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
        UIUtil.fadeOutAll(600, canvasPane, scrollPane, hBox);
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
    void onMouseClickedSearch(MouseEvent event) {
        UIUtil.fadeOutAll(600, canvasPane, scrollPane, hBox);
        TranslateTransition tt = UIUtil.move(600, lowerPane.getTranslateX(), lowerPane.getTranslateY(), -220, lowerPane.getTranslateY(), lowerPane);
        UIUtil.move(400, upperPane.getTranslateX(), upperPane.getTranslateY(), -50, upperPane.getTranslateY(), upperPane).play();
        tt.setOnFinished(ActionEvent -> {
            try {
                at.stop();
                main.showSearchPage();
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
                PlayerOnSale playerOnSale = new PlayerOnSale(player, (int) (player.getSalary() * (1 + new Random().nextDouble())));
                main.getSocket().write(new SellMessageClient(playerOnSale));
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


    @FXML
    void onMouseEnteredBuy(MouseEvent event) {
        UIUtil.mouseEnteredEffectDark(buyButton);
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
    void onMouseEnteredSearch(MouseEvent event) {
        UIUtil.mouseEnteredEffectDark(searchButton);
    }

    @FXML
    void onMouseEnteredSell(MouseEvent event) {
        UIUtil.mouseEnteredEffectSpecial(sellButton);
    }

    @FXML
    void onMouseEnteredYes(MouseEvent event) {
        UIUtil.mouseEnteredEffectLight(yesButton);
    }

    @FXML
    void onMouseEnteredDetails(MouseEvent event) {
        detailsButton.setStyle("-fx-background-color: #b9b6b6; -fx-text-fill:  black ; -fx-border-color: transparent;");
    }

    @FXML
    void onMouseEnteredNo(MouseEvent event) {
        UIUtil.mouseEnteredEffectLight(noButton);
    }


    @FXML
    void onMouseExitedBuy(MouseEvent event) {
        UIUtil.mouseExitedEffectDark(buyButton);
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
    void onMouseExitedSearch(MouseEvent event) {
        UIUtil.mouseExitedEffectDark(searchButton);
    }

    @FXML
    void onMouseExitedSell(MouseEvent event) {
        UIUtil.mouseExitedEffectSpecial(sellButton);
    }

    @FXML
    void onMouseExitedYes(MouseEvent event) {
        UIUtil.mouseExitedEffectLight(yesButton);
    }

    @FXML
    void onMouseExitedDetails(MouseEvent event) {
        detailsButton.setStyle("-fx-background-color: #dfdfdf; -fx-text-fill:  black ; -fx-border-color: transparent;");
    }

    @FXML
    void onMouseExitedNo(MouseEvent event) {
        UIUtil.mouseExitedEffectLight(noButton);
    }


    private void loadTable() {
        ObservableList<Player> observableList = FXCollections.observableArrayList();
        observableList.addAll(club.getList());

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
        hBox.setEffect(new GaussianBlur());
        UIUtil.fadeInAll(500, alertPane);
        alertPane.setDisable(false);
    }

    private void alertClose() {
        scrollPane.setEffect(null);
        upperPane.setEffect(null);
        lowerPane.setEffect(null);
        canvasPane.setEffect(null);
        hBox.setEffect(null);
        UIUtil.fadeOutAll(300, alertPane);
        alertPane.setDisable(true);
    }

    void initialize() {
        this.club = main.getClub();
        loadTable();
        clubNameLabel.setText(club.getName());
    }

    void setMain(Main main) {
        this.main = main;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UIUtil.highlightButton(homeButton);
        UIUtil.fadeInAll(600, canvasPane, scrollPane, hBox);
        TranslateTransition t1 = UIUtil.move(800, lowerPane.getTranslateX(), lowerPane.getTranslateY(), 0, lowerPane.getTranslateY(), lowerPane);
        TranslateTransition t2 = UIUtil.move(400, upperPane.getTranslateX(), upperPane.getTranslateY(), 0, upperPane.getTranslateY(), upperPane);
        new SequentialTransition(t1, t2).play();
        at = UIUtil.draw(canvas, 10);
    }
}
