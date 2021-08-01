package controller;

import database.Player;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import database.PlayerOnSale;

public class PlayerPageController {

    private Main main;
    private Player player;
    private PlayerOnSale playerOnSale;
    private AnimationTimer at;

    @FXML
    private Label nameField, numberField, positionField, countryField, ageField, heightField, salaryField, clubField, priceFieldLeft, priceFieldRight;
    @FXML
    private Canvas canvas;

    void load() {
        Player p = (player != null) ? player : playerOnSale;

        nameField.setText(p.getName());
        numberField.setText(String.valueOf(p.getNumber()));
        countryField.setText(p.getCountry());
        clubField.setText(p.getClub());
        positionField.setText(p.getPosition());
        ageField.setText(String.valueOf(p.getAge()));
        heightField.setText(String.valueOf(p.getHeight()));
        salaryField.setText(String.valueOf(p.getSalary()));

        if (player == null) {
            priceFieldLeft.setVisible(true);
            priceFieldRight.setVisible(true);
            priceFieldRight.setText(String.format("%.2f", playerOnSale.getPrice()));
        }
    }

    void setMain(Main main) {
        this.main = main;
    }

    // Two overloaded constructors - One for playerOnSale, another for Player
    void initialize(PlayerOnSale player) {
        this.playerOnSale = player;
        this.player = null;
        load();
        at = UIUtil.draw(canvas, 7);
        at.start();
    }

    void initialize(Player player) {
        this.player = player;
        this.playerOnSale = null;
        load();
        at = UIUtil.draw(canvas, 7);
        at.start();
    }
}
