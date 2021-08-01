package database;

import java.util.ArrayList;
import java.util.List;

public class PlayerSearch {
    public static List<Player> searchByName(String name, List<Player> playerList) {
        List<Player> found = new ArrayList<Player>();
        for (Player player : playerList)
            if (player.getName().equalsIgnoreCase(name))
                found.add(player);
        return found;
    }

    public static List<Player> searchByClub(String club, List<Player> playerList) {
        List<Player> found = new ArrayList<Player>();
        for (Player player : playerList)
            if (player.getClub().equalsIgnoreCase(club))
                found.add(player);
        return found;
    }

    public static List<Player> searchByCountry(String country, List<Player> playerList) {
        List<Player> found = new ArrayList<Player>();
        for (Player player : playerList)
            if (player.getCountry().equalsIgnoreCase(country))
                found.add(player);
        return found;
    }

    public static List<Player> searchByPosition(String position, List<Player> playerList) {
        List<Player> found = new ArrayList<Player>();
        for (Player player : playerList)
            if (player.getPosition().equalsIgnoreCase(position))
                found.add(player);
        return found;
    }

    public static List<Player> searchBySalary(double lower, double upper, List<Player> playerList) {
        List<Player> found = new ArrayList<Player>();
        for (Player player : playerList)
            if (player.getSalary() >= lower && player.getSalary() <= upper)
                found.add(player);
        return found;
    }

}
