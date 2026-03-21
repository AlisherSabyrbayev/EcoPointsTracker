package kz.iitu.service;

import kz.iitu.model.Household;
import kz.iitu.model.RecyclingEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {
    private HashMap<String, Household> households;
    private HashMap<String, ArrayList<RecyclingEvent>> recyclingEvents;

    public DataManager() {
        this.households = new HashMap<>();
        this.recyclingEvents = new HashMap<>();
    }

    public void registerHousehold(String id, String name, String address) {
        if (households.containsKey(id)) {
            throw new IllegalArgumentException("Household with ID '" + id + "' already exists.");
        }
        Household household = new Household(id, name, address, LocalDate.now());
        households.put(id, household);
        recyclingEvents.put(id, new ArrayList<>());
    }

    public void logRecyclingEvent(String householdId, String material, double weightKg, LocalDate date) {
        if (!households.containsKey(householdId)) {
            throw new IllegalArgumentException("Household with ID '" + householdId + "' not found.");
        }

        if (weightKg <= 0) {
            throw new IllegalArgumentException("Weight must be a positive number. Got: " + weightKg);
        }

        if (!RecyclingEvent.isValidMaterial(material)) {
            throw new IllegalArgumentException("Invalid material type: '" + material
                    + "'. Valid types: PLASTIC, PAPER, GLASS, METAL.");
        }

        RecyclingEvent event = new RecyclingEvent(householdId, material, weightKg, date);
        recyclingEvents.get(householdId).add(event);
        households.get(householdId).addPoints(event.getEcoPointsEarned());
    }

    public List<Household> getAllHouseholds() {
        return new ArrayList<>(households.values());
    }

    public Household getHousehold(String id) {
        return households.get(id);
    }

    public List<RecyclingEvent> getEvents(String householdId) {
        return recyclingEvents.getOrDefault(householdId, new ArrayList<>());
    }

    public double getTotalWeight(String householdId) {
        double total = 0;
        for (RecyclingEvent event : getEvents(householdId)) {
            total += event.getWeightKg();
        }
        return total;
    }

    public Household getHouseholdWithHighestPoints() {
        Household best = null;
        for (Household h : households.values()) {
            if (best == null || h.getTotalPoints() > best.getTotalPoints()) {
                best = h;
            }
        }
        return best;
    }

    public double getCommunityTotalWeight() {
        double total = 0;
        for (ArrayList<RecyclingEvent> events : recyclingEvents.values()) {
            for (RecyclingEvent event : events) {
                total += event.getWeightKg();
            }
        }
        return total;
    }

    public Map<String, Household> getHouseholdsMap() {
        return households;
    }

    public Map<String, ArrayList<RecyclingEvent>> getRecyclingEventsMap() {
        return recyclingEvents;
    }

    public void setHouseholdsMap(HashMap<String, Household> households) {
        this.households = households;
    }

    public void setRecyclingEventsMap(HashMap<String, ArrayList<RecyclingEvent>> recyclingEvents) {
        this.recyclingEvents = recyclingEvents;
    }
}
