package com.epam.volunteer.entity;

public class Medicament {
    private long id;
    private String medicament;
    private int requirement;
    private int currentCount;
    private boolean relevance;


    public Medicament() {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMedicament() {
        return medicament;
    }

    public void setMedicament(String medicament) {
        this.medicament = medicament;
    }

    public int getRequirement() {
        return requirement;
    }

    public void setRequirement(int requirement) {
        this.requirement = requirement;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public boolean isRelevance() {
        return relevance;
    }

    public void setRelevance(boolean relevance) {
        this.relevance = relevance;
    }
}
