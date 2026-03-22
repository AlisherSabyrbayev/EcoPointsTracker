package kz.iitu;

import kz.iitu.model.Household;
import kz.iitu.model.RecyclingEvent;
import kz.iitu.service.DataManager;
import kz.iitu.service.FileManager;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final DataManager dataManager = new DataManager();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        FileManager.loadData(dataManager);

        System.out.println("==============================================");
        System.out.println("   Welcome to the EcoPoints Recycling Tracker");
        System.out.println("==============================================");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> registerHousehold();
                case "2" -> logRecyclingEvent();
                case "3" -> viewAllHouseholds();
                case "4" -> viewRecyclingEvents();
                case "5" -> viewTotalWeight();
                case "6" -> viewTotalPoints();
                case "7" -> generateReports();
                case "8" -> {
                    FileManager.saveData(dataManager);
                    System.out.println("Goodbye! Keep recycling!");
                    running = false;
                }
                default -> System.out.println("Invalid option. Please enter a number between 1 and 8.");
            }
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("---------- MAIN MENU ----------");
        System.out.println("1. Register a Household");
        System.out.println("2. Log a Recycling Event");
        System.out.println("3. View All Households");
        System.out.println("4. View Recycling Events for a Household");
        System.out.println("5. View Total Weight Recycled by a Household");
        System.out.println("6. View Total Eco Points for a Household");
        System.out.println("7. Generate Reports");
        System.out.println("8. Exit");
        System.out.print("Select an option: ");
    }

    private static void registerHousehold() {
        try {
            System.out.print("Enter Household ID: ");
            String id = scanner.nextLine().trim();

            System.out.print("Enter Household Name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter Address: ");
            String address = scanner.nextLine().trim();

            if (id.isEmpty() || name.isEmpty() || address.isEmpty()) {
                System.out.println("Error: All fields are required.");
                return;
            }

            dataManager.registerHousehold(id, name, address);
            FileManager.saveData(dataManager);
            System.out.println("Household registered successfully! Join date: " + LocalDate.now());

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void logRecyclingEvent() {
        try {
            System.out.print("Enter Household ID: ");
            String householdId = scanner.nextLine().trim();

            System.out.print("Enter Material Type (PLASTIC, PAPER, GLASS, METAL): ");
            String material = scanner.nextLine().trim();

            System.out.print("Enter Weight in kg: ");
            String weightInput = scanner.nextLine().trim();
            double weight;
            try {
                weight = Double.parseDouble(weightInput);
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid weight. Please enter a valid number.");
                return;
            }

            System.out.print("Enter Date of recycling (YYYY-MM-DD) or press Enter for today: ");
            String dateInput = scanner.nextLine().trim();
            LocalDate date;
            if (dateInput.isEmpty()) {
                date = LocalDate.now();
            } else {
                try {
                    date = LocalDate.parse(dateInput);
                } catch (DateTimeParseException e) {
                    System.out.println("Error: Invalid date format. Please use YYYY-MM-DD.");
                    return;
                }
            }

            dataManager.logRecyclingEvent(householdId, material, weight, date);
            FileManager.saveData(dataManager);
            System.out.printf("Recycling event logged! Points earned: %.1f%n", weight * RecyclingEvent.POINTS_PER_KG);

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewAllHouseholds() {
        List<Household> households = dataManager.getAllHouseholds();
        if (households.isEmpty()) {
            System.out.println("No households registered yet.");
            return;
        }
        System.out.println("\n--- All Registered Households ---");
        for (Household h : households) {
            System.out.println(h);
        }
    }

    private static void viewRecyclingEvents() {
        System.out.print("Enter Household ID: ");
        String id = scanner.nextLine().trim();

        Household h = dataManager.getHousehold(id);
        if (h == null) {
            System.out.println("Error: Household with ID '" + id + "' not found.");
            return;
        }

        List<RecyclingEvent> events = dataManager.getEvents(id);
        if (events.isEmpty()) {
            System.out.println("No recycling events recorded for household '" + id + "'.");
            return;
        }

        System.out.println("\n--- Recycling Events for " + h.getName() + " (" + id + ") ---");
        for (RecyclingEvent event : events) {
            System.out.println(event);
        }
    }

    private static void viewTotalWeight() {
        System.out.print("Enter Household ID: ");
        String id = scanner.nextLine().trim();

        Household h = dataManager.getHousehold(id);
        if (h == null) {
            System.out.println("Error: Household with ID '" + id + "' not found.");
            return;
        }

        double totalWeight = dataManager.getTotalWeight(id);
        System.out.printf("Total weight recycled by %s: %.2f kg%n", h.getName(), totalWeight);
    }

    private static void viewTotalPoints() {
        System.out.print("Enter Household ID: ");
        String id = scanner.nextLine().trim();

        Household h = dataManager.getHousehold(id);
        if (h == null) {
            System.out.println("Error: Household with ID '" + id + "' not found.");
            return;
        }

        System.out.printf("Total Eco Points for %s: %.1f%n", h.getName(), h.getTotalPoints());
    }

    private static void generateReports() {
        System.out.println("\n--- Community Reports ---");

        Household top = dataManager.getHouseholdWithHighestPoints();
        if (top != null) {
            System.out.printf("Household with highest points: %s (ID: %s) — %.1f points%n",
                    top.getName(), top.getId(), top.getTotalPoints());
        } else {
            System.out.println("No households registered yet.");
        }

        double communityWeight = dataManager.getCommunityTotalWeight();
        System.out.printf("Total community recycling weight: %.2f kg%n", communityWeight);
    }
}