package database;

import java.io.Serializable;
import java.util.Objects;

public class Player implements Serializable {
    protected String name, country, club, position;
    protected int age, number;
    protected double height, salary;

    public void setAll(Player p) {
        name = p.name;
        country = p.country;
        club = p.club;
        position = p.position;
        age = p.age;
        number = p.number;
        height = p.height;
        salary = p.salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

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
    }

    public String toString() {
        String out = "Name: " + name +
                "\nCountry: " + country +
                "\nAge: " + age +
                "\nHeight: " + height +
                "\nClub: " + club +
                "\nPosition: " + position +
                "\nNumber: " + number +
                "\nWeekly Salary: " + salary + "\n\n";
        return out;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
