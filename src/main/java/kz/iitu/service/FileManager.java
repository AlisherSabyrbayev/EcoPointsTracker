package kz.iitu.service;

import kz.iitu.model.Household;
import kz.iitu.model.RecyclingEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileManager {

    private static final String DATA_DIR = "data";
    private static final String HOUSEHOLDS_FILE = DATA_DIR + File.separator + "households.txt";
    private static final String EVENTS_FILE = DATA_DIR + File.separator + "recycling_events.txt";

    public static void saveData(DataManager dataManager) {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HOUSEHOLDS_FILE))) {
            for (Household h : dataManager.getHouseholdsMap().values()) {
                writer.write(h.toFileString());
                writer.newLine();
            }
            System.out.println("Households saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving households: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(EVENTS_FILE))) {
            for (Map.Entry<String, ArrayList<RecyclingEvent>> entry : dataManager.getRecyclingEventsMap().entrySet()) {
                for (RecyclingEvent event : entry.getValue()) {
                    writer.write(event.toFileString());
                    writer.newLine();
                }
            }
            System.out.println("Recycling events saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving recycling events: " + e.getMessage());
        }
    }

    public static void loadData(DataManager dataManager) {
        HashMap<String, Household> households = new HashMap<>();
        HashMap<String, ArrayList<RecyclingEvent>> events = new HashMap<>();

        File householdsFile = new File(HOUSEHOLDS_FILE);
        if (householdsFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(HOUSEHOLDS_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        Household h = Household.fromFileString(line);
                        households.put(h.getId(), h);
                        events.put(h.getId(), new ArrayList<>());
                    }
                }
                System.out.println("Households loaded successfully.");
            } catch (IOException e) {
                System.out.println("Error loading households: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error parsing household data: " + e.getMessage());
            }
        }

        File eventsFile = new File(EVENTS_FILE);
        if (eventsFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(EVENTS_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        RecyclingEvent event = RecyclingEvent.fromFileString(line);
                        String hId = event.getHouseholdId();
                        events.computeIfAbsent(hId, k -> new ArrayList<>()).add(event);
                    }
                }
                System.out.println("Recycling events loaded successfully.");
            } catch (IOException e) {
                System.out.println("Error loading recycling events: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error parsing recycling event data: " + e.getMessage());
            }
        }

        dataManager.setHouseholdsMap(households);
        dataManager.setRecyclingEventsMap(events);
    }
}
