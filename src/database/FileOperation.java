package database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class FileOperation {
    public static List<String> read(String fileName) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        List<String> lines = new ArrayList<>();
        while (true) {
            String line = br.readLine();
            if (line == null) break;
            lines.add(line);
        }
        br.close();
        return lines;
    }

    public static void writePlayer(List<Player> playerList, String fileName) throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
        for (Player player : playerList) {
            bw.write(player.getName()+","+player.getCountry()+","+player.getAge()+","+player.getHeight()+","+player.getClub()+","+player.getPosition()+","+player.getNumber()+","+player.getSalary()+"\n");
        }
        bw.close();
    }

    public static void writePlayerOnSale(List<PlayerOnSale> playerList, String fileName) throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
        for (PlayerOnSale player : playerList) {
            bw.write(player.getName()+","+player.getCountry()+","+player.getAge()+","+player.getHeight()+","+player.getClub()+","+player.getPosition()+","+player.getNumber()+","+player.getSalary()+","+player.getPrice()+"\n");
        }
        bw.close();
    }

}
