package database;

import java.util.ArrayList;
import java.util.List;

public class PlayerList {
    private List<Player> playerList = new ArrayList<>();
    private List<PlayerOnSale> playerOnSaleList = new ArrayList<>();
    String fileName1 = "src/files/players.txt";
    String fileName2 = "src/files/playersOnSale.txt";

    public void load() {
        List<String> stringList = null;
        try {
            stringList = FileOperation.read(fileName1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String line : stringList) {
            Player player = new Player();
            player.setFromString(line);
            playerList.add(player);
        }

        stringList.clear();
        try {
            stringList = FileOperation.read(fileName2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String line : stringList) {
            PlayerOnSale playerOnSale = new PlayerOnSale();
            playerOnSale.setFromString(line);
            playerOnSaleList.add(playerOnSale);
        }
    }

    public void save() {
        try {
            FileOperation.writePlayer(playerList, fileName1);
            FileOperation.writePlayerOnSale(playerOnSaleList, fileName2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Club searchClub(String clubName) {
        Club club = new Club(clubName);
        for (Player player : playerList) {
            if (player.getClub().equalsIgnoreCase(clubName)) {
                club.setName(player.getClub());
                club.add(player);
            }
        }
        if (club.getCount() > 0)
            return club;
        return null;
    }

    public boolean updatePlayer(Player player) {
        for (Player p : playerList)
            if (p.equals(player)) {
                p.setAll(player);
                return true;
            }
        return false;
    }

    public List<PlayerOnSale> getPlayerOnSaleList() {
        return playerOnSaleList;
    }
}
