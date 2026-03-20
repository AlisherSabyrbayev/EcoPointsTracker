package kz.iitu.model;

import java.time.LocalDate;

public class RecyclingEvent {
    private String householdId;
    private String materialType;
    private double weightKg;
    private LocalDate date;
    private double ecoPointsEarned;

    public static final double POINTS_PER_KG = 10.0;
    public static final String[] VALID_MATERIALS = {"PLASTIC", "PAPER", "GLASS", "METAL"};

    public RecyclingEvent(String householdId, String materialType, double weightKg, LocalDate date) {
        this.householdId = householdId;
        this.materialType = materialType.toUpperCase();
        this.weightKg = weightKg;
        this.date = date;
        this.ecoPointsEarned = weightKg * POINTS_PER_KG;
    }

    public String getHouseholdId() {
        return householdId;
    }

    public String getMaterialType() {
        return materialType;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getEcoPointsEarned() {
        return ecoPointsEarned;
    }

    public void setHouseholdId(String householdId) {
        this.householdId = householdId;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public void setWeightKg(double weightKg) {
        this.weightKg = weightKg;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setEcoPointsEarned(double ecoPointsEarned) {
        this.ecoPointsEarned = ecoPointsEarned;
    }

    public static boolean isValidMaterial(String material) {
        for (String valid : VALID_MATERIALS) {
            if (valid.equalsIgnoreCase(material)) {
                return true;
            }
        }
        return false;
    }

    public String toFileString() {
        return householdId + "|" + materialType + "|" + weightKg + "|" + date + "|" + ecoPointsEarned;
    }

    public static RecyclingEvent fromFileString(String line) {
        String[] parts = line.split("\\|");
        RecyclingEvent event = new RecyclingEvent(parts[0], parts[1],
                Double.parseDouble(parts[2]), LocalDate.parse(parts[3]));
        event.setEcoPointsEarned(Double.parseDouble(parts[4]));
        return event;
    }

    @Override
    public String toString() {
        return String.format("RecyclingEvent[Household=%s, Material=%s, Weight=%.2f kg, Date=%s, Points=%.1f]",
                householdId, materialType, weightKg, date, ecoPointsEarned);
    }
}
