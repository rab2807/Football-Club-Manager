package database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Club implements Serializable {
    private int count;
    private List<Player> list = new ArrayList<>();
    private List<Player> maxSalaryPlayers = new ArrayList<>();
    private List<Player> maxHeightPlayers = new ArrayList<>();
    private List<Player> maxAgePlayers = new ArrayList<>();

    private HashMap<String, Integer> countryHashMap = new HashMap<>();
    private HashMap<String, Integer> positionHashMap = new HashMap<>();
    private String name;

    Club(String name) {
        this.name = name;
        count = 0;
    }

    public void add(Player p) {
        list.add(p);
        count++;
        updateData();
    }

    private void updateData() {
        double max = -1;
        for (Player player : list)
            if (player.getSalary() >= max) {
                if (player.getSalary() > max)
                    maxSalaryPlayers.clear();
                maxSalaryPlayers.add(player);
                max = player.getSalary();
            }

        max = -1;
        for (Player player : list)
            if (player.getHeight() >= max) {
                if (player.getHeight() > max)
                    maxHeightPlayers.clear();
                maxHeightPlayers.add(player);
                max = player.getHeight();
            }

        max = -1;
        for (Player player : list)
            if (player.getAge() >= max) {
                if (player.getAge() > max)
                    maxAgePlayers.clear();
                maxAgePlayers.add(player);
                max = player.getAge();
            }

        countryHashMap.clear();
        positionHashMap.clear();
        for (Player player : list) {
            String countryName = player.getCountry();
            if (countryHashMap.containsKey(countryName))
                countryHashMap.put(countryName, countryHashMap.get(countryName) + 1);
            else
                countryHashMap.put(countryName, 1);

            String positionName = player.getPosition();
            if (positionHashMap.containsKey(positionName))
                positionHashMap.put(positionName, positionHashMap.get(positionName) + 1);
            else
                positionHashMap.put(positionName, 1);
        }

    }

    public void removePlayer(Player player) {
        if (player == null)
            return;
        list.remove(player);
        updateData();
    }

    public List<Player> getMaxSalaryPlayers() {
        return maxSalaryPlayers;
    }

    public List<Player> getMaxHeightPlayers() {
        return maxHeightPlayers;
    }

    public List<Player> getMaxAgePlayers() {
        return maxAgePlayers;
    }

    public HashMap<String, Integer> getCountryHashMap() {
        return countryHashMap;
    }

    public HashMap<String, Integer> getPositionHashMap() {
        return positionHashMap;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public List<Player> getList() {
        return list;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double totalSalary() {
        double total = 0;
        for (Player player : list)
            total += player.getSalary() * 52;
        return total;
    }
}
