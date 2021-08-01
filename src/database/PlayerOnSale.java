package database;

import database.Player;

import java.io.Serializable;

public class PlayerOnSale extends Player implements Serializable {
    private double price;
    private Player player;

    public PlayerOnSale() {
        price = -1;
        player = new Player();
    }

    public PlayerOnSale(Player player, double price) {
        setAll(player);
        this.price = price;
        this.player = player;
    }

    @Override
    public void setFromString(String input) {
        String[] lines = input.split(",");
        int i = 0;
        name = lines[i++];
        country = lines[i++];
        age = Integer.parseInt(lines[i++]);
        height = Double.parseDouble(lines[i++]);
        club = lines[i++];
        position = lines[i++];
        number = Integer.parseInt(lines[i++]);
        salary = Double.parseDouble(lines[i++]);
        price = Double.parseDouble(lines[i]);
        player.setFromString(input);
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Player getPlayer() {
        return player;
    }
}
