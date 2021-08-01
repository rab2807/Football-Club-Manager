package controller;

import database.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import messagetype.AuctionPageMessage;
import messagetype.ExitRequest;
import database.PlayerOnSale;
import server.SocketWrapper;

import java.io.IOException;

public class Main extends Application {

    private Club club;
    private Stage stage;
    private static SocketWrapper socket;

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public SocketWrapper getSocket() {
        return socket;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        showLoginPage();
    }

    void showLoginPage() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXMLfile/LoginPage.fxml"));
        Parent root = loader.load();

        LoginPageController controller = loader.getController();
        controller.setMain(this);

        stage.setOnCloseRequest(e -> windowCloseAction());
        stage.setTitle("Player Database System");
        stage.setScene(new Scene(root, 1000, 700));
        stage.show();
    }

    void showHomePage() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXMLfile/HomePage.fxml"));
        Parent root = loader.load();

        HomePageController controller = loader.getController();
        controller.setMain(this);
        controller.initialize();

        stage.setOnCloseRequest(e -> windowCloseAction());
        stage.setTitle("Player Database System");
        stage.setScene(new Scene(root, 1000, 700));
        stage.show();
    }

    void showSearchPage() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXMLfile/SearchPage.fxml"));
        Parent root = loader.load();

        SearchPageController controller = loader.getController();
        controller.setMain(this);
        controller.initialize();

        stage.setOnCloseRequest(e -> windowCloseAction());
        stage.setTitle("Player Database System");
        stage.setScene(new Scene(root, 1000, 700));
        stage.show();
    }

    void showStatsPage() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXMLfile/StatsPage.fxml"));
        Parent root = loader.load();

        StatsPageController controller = loader.getController();
        controller.setMain(this);
        controller.initialize();

        stage.setOnCloseRequest(e -> windowCloseAction());
        stage.setTitle("Player Database System");
        stage.setScene(new Scene(root, 1000, 700));
        stage.show();
    }

    void showBuyPage() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXMLfile/BuyPage.fxml"));
        Parent root = loader.load();

        BuyPageController controller = loader.getController();
        controller.setMain(this);
        controller.initialize();

        stage.setOnCloseRequest(e -> {
            try {
                socket.write(new AuctionPageMessage(club.getName(), false));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            windowCloseAction();
        });
        stage.setTitle("Player Database System");
        stage.setScene(new Scene(root, 1000, 700));
        stage.show();
    }

    void showPlayerPage(Player player) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXMLfile/PlayerPage.fxml"));
        Parent root = loader.load();
        Stage playerStage = new Stage();

        PlayerPageController controller = loader.getController();
        controller.setMain(this);
        controller.initialize(player);

        playerStage.initModality(Modality.APPLICATION_MODAL);
        playerStage.setTitle("Player Details");
        playerStage.setScene(new Scene(root, 600, 500));
        playerStage.showAndWait();
    }

    void showPlayerPage(PlayerOnSale player) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXMLfile/PlayerPage.fxml"));
        Parent root = loader.load();
        Stage playerStage = new Stage();

        PlayerPageController controller = loader.getController();
        controller.setMain(this);
        controller.initialize(player);

        playerStage.initModality(Modality.APPLICATION_MODAL);
        playerStage.setTitle("Player Details");
        playerStage.setScene(new Scene(root, 600, 500));
        playerStage.show();
    }

    void windowCloseAction() {
        // Upon closing, a message to server is send to terminate the server read thread associated with this client.
        // Then socket and input-output stream is closed.
        try {
            String name = "";
            if (club != null)
                name = club.getName();
            socket.write(new ExitRequest(name));
            socket.close();
            socket.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void startClient(String address, int port) {
        System.out.println("Client started ... ");
        socket = new SocketWrapper(address, port);
    }

    public static void main(String[] args) {
        startClient("127.0.0.1", 22222);
        launch(args);
    }
}
