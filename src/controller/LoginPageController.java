package controller;

import database.Club;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import messagetype.LoginMessageClient;
import messagetype.LoginMessageServer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginPageController implements Initializable {

    private Main main;
    private Club club;
    AnimationTimer at;

    @FXML
    private Label errorLabel;
    @FXML
    private AnchorPane upperPane, lowerPane;
    @FXML
    private TextField usernameField;
    @FXML
    private Button submitButton, resetButton;
    @FXML
    private VBox welcomeMsg;
    @FXML
    private Canvas canvas;
    @FXML
    private Pane canvasPane;

    @FXML
    public void onMouseClickedSubmitButton(MouseEvent event) {
        String name = usernameField.getText().trim();
        login(name);
    }

    @FXML
    public void onMouseClickedResetButton(MouseEvent event) {
        usernameField.setText("");
    }

    @FXML
    public void onMouseEnteredSubmitButton(MouseEvent event) {
        UIUtil.mouseEnteredEffectDark(submitButton);
    }

    @FXML
    public void onMouseEnteredResetButton(MouseEvent event) {
        UIUtil.mouseEnteredEffectDark(resetButton);
    }

    @FXML
    public void onMouseExitedSubmitButton(MouseEvent event) {
        UIUtil.mouseExitedEffectDark(submitButton);
    }

    @FXML
    public void onMouseExitedResetButton(MouseEvent event) {
        UIUtil.mouseExitedEffectDark(resetButton);
    }

    @FXML
    public void onKeyPressedUserNameField(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            String name = usernameField.getText().trim();
            login(name);
        }
    }

    private void login(String name) {
        // Send the user input to server. Server will return the club of that input.
        // If input is invalid, club is null and error is shown. Otherwise, client can access its club and player list.
        try {
            main.getSocket().write(new LoginMessageClient(name, true, main.getSocket()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        LoginMessageServer message = null;
        try {
            message = (LoginMessageServer) main.getSocket().read();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        club = message.getClub();

        if (club != null) {
            main.setClub(club);
            name = club.getName();
//            System.out.println(name);

            UIUtil.move(550, upperPane.getTranslateX(), upperPane.getTranslateY(), -540, upperPane.getTranslateY(), upperPane).play();
            UIUtil.move(800, lowerPane.getTranslateX(), lowerPane.getTranslateY(), -540, lowerPane.getTranslateY(), lowerPane).play();
            UIUtil.fadeOut(1500, canvasPane).play();
            ((Label) welcomeMsg.getChildren().get(1)).setText(name);
            FadeTransition f1 = UIUtil.fadeIn(1000, welcomeMsg);
            PauseTransition pt = new PauseTransition(Duration.millis(1200));
            FadeTransition f2 = UIUtil.fadeOut(1000, welcomeMsg);
            SequentialTransition st = new SequentialTransition(f1, pt, f2);
            st.setOnFinished(actionEvent -> {
                try {
                    at.stop();
                    main.showHomePage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            st.play();
        } else {
//            System.out.println("null");
            errorLabel.setText(message.getMessage());
            UIUtil.fadeOut(2000, errorLabel).play();
            UIUtil.shakeAll(80, usernameField, submitButton, resetButton);
        }
    }

    void setMain(Main main) {
        this.main = main;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UIUtil.fadeInAll(900, canvasPane);
        UIUtil.move(900, upperPane.getTranslateX(), upperPane.getTranslateY(), 0, upperPane.getTranslateY(), upperPane).play();
        UIUtil.move(900, lowerPane.getTranslateX(), lowerPane.getTranslateY(), 0, lowerPane.getTranslateY(), lowerPane).play();
        at = UIUtil.draw(canvas, 20);
    }
}