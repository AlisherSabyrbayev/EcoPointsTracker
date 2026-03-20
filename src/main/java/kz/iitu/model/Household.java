package kz.iitu.model;

import java.time.LocalDate;

public class Household {
    private String id;
    private String name;
    private String address;
    private LocalDate joiningDate;
    private double totalPoints;

    public Household(String id, String name, String address, LocalDate joiningDate) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.joiningDate = joiningDate;
        this.totalPoints = 0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public double getTotalPoints() {
        return totalPoints;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public void setTotalPoints(double totalPoints) {
        this.totalPoints = totalPoints;
    }

    public void addPoints(double points) {
        this.totalPoints += points;
    }

    public String toFileString() {
        return id + "|" + name + "|" + address + "|" + joiningDate + "|" + totalPoints;
    }

    public static Household fromFileString(String line) {
        String[] parts = line.split("\\|");
        Household h = new Household(parts[0], parts[1], parts[2], LocalDate.parse(parts[3]));
        h.setTotalPoints(Double.parseDouble(parts[4]));
        return h;
    }

    @Override
    public String toString() {
        return String.format("Household[ID=%s, Name=%s, Address=%s, Joined=%s, Points=%.1f]",
                id, name, address, joiningDate, totalPoints);
    }
}
