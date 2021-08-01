package controller;

import database.Club;
import database.PlayerOnSale;
import javafx.animation.AnimationTimer;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import messagetype.*;
import server.SocketWrapper;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

class AuctionListReadThread implements Runnable {
    private SocketWrapper socket;
    private Thread thread;
    private BuyPageController controller;

    AuctionListReadThread(SocketWrapper socket, BuyPageController controller) {
        System.out.println("AuctionListReadThread started");
        this.socket = socket;
        this.controller = controller;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            Object obj = null;
            try {
                obj = socket.read();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            // Check for updates of auction list. When a new auction list is caught, reload the table.
            // Null auction list will terminate the thread.
            if (obj instanceof AuctionList) {
                AuctionList auctionList = (AuctionList) obj;
                if (auctionList.getPlayers() != null)
                    controller.loadTable(auctionList.getPlayers());
                else break;
            }
        }
        System.out.println("AuctionListReadThread closed");
    }
}

public class BuyPageController implements Initializable {

    private Main main;
    private Club club;
    private AnimationTimer at;

    @FXML
    private Button searchButton, buyButton, maxButton, logoutButton, homeButton, buyPlayerButton, detailsButton, yesButton, noButton;
    @FXML
    private TableView<PlayerOnSale> table;
    @FXML
    private TableColumn<PlayerOnSale, String> nameCol, positionCol, countryCol;
    @FXML
    private TableColumn<PlayerOnSale, Double> priceCol, salaryCol;
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
    void onMouseClickedHome(MouseEvent event) {
        try {
            main.getSocket().write(new AuctionPageMessage(club.getName(), false));
        } catch (IOException e) {
            e.printStackTrace();
        }
        UIUtil.fadeOutAll(600, canvasPane, scrollPane, hBox);
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
        try {
            main.getSocket().write(new AuctionPageMessage(club.getName(), false));
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
        try {
            main.getSocket().write(new AuctionPageMessage(club.getName(), false));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        try {
            main.getSocket().write(new AuctionPageMessage(club.getName(), false));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    void onMouseClickedBuyPlayer(MouseEvent event) {
        alertOpen();
    }

    @FXML
    void onMouseClickedDetails(MouseEvent event) {
        PlayerOnSale player = table.getSelectionModel().getSelectedItem();
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
        // Change the club name of Player object and PlayerOnSale object to buyer club's name
        // send the changed PlayerOnSale object to server and add the player in club list
        PlayerOnSale playerOnSale = table.getSelectionModel().getSelectedItem();
        playerOnSale.setClub(club.getName());
        playerOnSale.getPlayer().setClub(club.getName());

        try {
            main.getSocket().write(new BuyMessageClient(playerOnSale));
        } catch (IOException e) {
            e.printStackTrace();
        }
        club.add(playerOnSale.getPlayer());
        alertClose();
    }

    @FXML
    void onMouseClickedNo(MouseEvent event) {
        alertClose();
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
    void onMouseEnteredSearch(MouseEvent event) {
        UIUtil.mouseEnteredEffectDark(searchButton);
    }

    @FXML
    void onMouseEnteredBuyPlayer(MouseEvent event) {
        UIUtil.mouseEnteredEffectSpecial(buyPlayerButton);
    }

    @FXML
    void onMouseEnteredYes(MouseEvent mouseEvent) {
        UIUtil.mouseEnteredEffectLight(yesButton);
    }

    @FXML
    void onMouseEnteredDetails(MouseEvent event) {
        detailsButton.setStyle("-fx-background-color: #b9b6b6; -fx-text-fill:  black ; -fx-border-color: transparent;");
    }

    @FXML
    void onMouseEnteredNo(MouseEvent mouseEvent) {
        UIUtil.mouseEnteredEffectLight(noButton);
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
    void onMouseExitedSearch(MouseEvent event) {
        UIUtil.mouseExitedEffectDark(searchButton);
    }

    @FXML
    void onMouseExitedBuyPlayer(MouseEvent event) {
        UIUtil.mouseExitedEffectSpecial(buyPlayerButton);
    }

    @FXML
    void onMouseExitedYes(MouseEvent mouseEvent) {
        UIUtil.mouseExitedEffectLight(yesButton);
    }

    @FXML
    void onMouseExitedDetails(MouseEvent event) {
        detailsButton.setStyle("-fx-background-color: #dfdfdf; -fx-text-fill:  black ; -fx-border-color: transparent;");
    }

    @FXML
    void onMouseExitedNo(MouseEvent mouseEvent) {
        UIUtil.mouseExitedEffectLight(noButton);
    }


    void loadTable(List<PlayerOnSale> players) {
        ObservableList<PlayerOnSale> observableList = FXCollections.observableArrayList();
        observableList.addAll(players);

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        positionCol.setCellValueFactory(new PropertyValueFactory<>("position"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        salaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

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

        // send a message to server telling that the client has entered the auction page
        try {
            main.getSocket().write(new AuctionPageMessage(club.getName(), true));
        } catch (IOException e) {
            e.printStackTrace();
        }

        clubNameLabel.setText(club.getName());
        new AuctionListReadThread(main.getSocket(), this);
    }

    void setMain(Main main) {
        this.main = main;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UIUtil.highlightButton(buyButton);
        UIUtil.fadeInAll(600, canvasPane, scrollPane, hBox);
        TranslateTransition t1 = UIUtil.move(800, lowerPane.getTranslateX(), lowerPane.getTranslateY(), 0, lowerPane.getTranslateY(), lowerPane);
        TranslateTransition t2 = UIUtil.move(400, upperPane.getTranslateX(), upperPane.getTranslateY(), 0, upperPane.getTranslateY(), upperPane);
        new SequentialTransition(t1, t2).play();
        at = UIUtil.draw(canvas, 10);
        at.start();
    }
}
