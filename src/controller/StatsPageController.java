package controller;

import database.Club;
import database.Player;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

enum StatMode {
    NONE, MAX_SALARY, MAX_AGE, MAX_HEIGHT, COUNTRY_COUNT, POSITION_COUNT
}

public class StatsPageController implements Initializable {

    private Main main;
    private Club club;
    private AnimationTimer at;

    @FXML
    private Button homeButton, buyButton, maxButton, logoutButton, searchButton;
    @FXML
    private ComboBox<String> dropdown;
    @FXML
    private Label label, clubNameLabel;
    @FXML
    private Pane canvasPane;
    @FXML
    private AnchorPane lowerPane, upperPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Canvas canvas;

    private StatMode mode = StatMode.NONE;

    @FXML
    void setCountDropDown() {
        ObservableList<String> list = FXCollections.observableArrayList("Maximum Salary", "Maximum Age", "Maximum Height", "Country Count", "Position Count");
        dropdown.setItems(list);

        dropdown.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    switch (newValue) {
                        case "Maximum Salary" -> mode = StatMode.MAX_SALARY;
                        case "Maximum Age" -> mode = StatMode.MAX_AGE;
                        case "Maximum Height" -> mode = StatMode.MAX_HEIGHT;
                        case "Country Count" -> mode = StatMode.COUNTRY_COUNT;
                        case "Position Count" -> mode = StatMode.POSITION_COUNT;
                    }
                    loadLabel(mode);
                }
        );
    }

    private void loadLabel(StatMode mode) {
        FadeTransition ft = UIUtil.fadeOut(300, scrollPane);
        ft.setOnFinished(ActionEvent -> {
            setLabel(mode);
            UIUtil.fadeInAll(300, scrollPane);
        });
        ft.play();
    }

    public void setLabel(StatMode mode) {
        String text = "";
        if (mode == StatMode.MAX_SALARY) {
            List<Player> list = club.getMaxSalaryPlayers();
            for (Player p : list)
                text = text.concat(p.toString());
        } else if (mode == StatMode.MAX_AGE) {
            List<Player> list = club.getMaxAgePlayers();
            for (Player p : list)
                text = text.concat(p.toString());
        } else if (mode == StatMode.MAX_HEIGHT) {
            List<Player> list = club.getMaxHeightPlayers();
            for (Player p : list)
                text = text.concat(p.toString());
        } else if (mode == StatMode.COUNTRY_COUNT) {
            HashMap<String, Integer> map = club.getCountryHashMap();
            for (String str : map.keySet())
                text = text.concat(str + " : " + map.get(str) + "\n\n");

        } else {
            HashMap<String, Integer> map = club.getPositionHashMap();
            for (String str : map.keySet())
                text = text.concat(str + " : " + map.get(str) + "\n\n");
        }
        label.setText(text);
    }


    @FXML
    void onMouseClickedBuy(MouseEvent event) {
        UIUtil.fadeOutAll(600, canvasPane, scrollPane, dropdown);
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
        UIUtil.fadeOutAll(600, canvasPane, scrollPane, dropdown);
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
        UIUtil.fadeOutAll(600, canvasPane, scrollPane, dropdown);
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
    void onMouseClickedSearch(MouseEvent event) {
        UIUtil.fadeOutAll(600, canvasPane, scrollPane, dropdown);
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
    void onMouseEnteredSearch(MouseEvent event) {
        UIUtil.mouseEnteredEffectDark(searchButton);
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
    void onMouseExitedSearch(MouseEvent event) {
        UIUtil.mouseExitedEffectDark(searchButton);
    }

    void initialize() {
        this.club = main.getClub();
        setCountDropDown();
        clubNameLabel.setText(club.getName());
    }

    void setMain(Main main) {
        this.main = main;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UIUtil.highlightButton(maxButton);
        UIUtil.fadeInAll(600, canvasPane, scrollPane, dropdown);
        TranslateTransition t1 = UIUtil.move(600, lowerPane.getTranslateX(), lowerPane.getTranslateY(), 0, lowerPane.getTranslateY(), lowerPane);
        TranslateTransition t2 = UIUtil.move(400, upperPane.getTranslateX(), upperPane.getTranslateY(), 0, upperPane.getTranslateY(), upperPane);
        new SequentialTransition(t1, t2).play();

        at = UIUtil.draw(canvas, 10);
        at.start();
    }
}
