package com.epam.volunteer.dto;


public class MedicamentDTO extends AbstractDTO {
    private String medicament;
    private int requirement;
    private int currentCount;
    private VolunteerDTO volunteer;
    private boolean status;

    public MedicamentDTO() {
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

    public VolunteerDTO getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(VolunteerDTO volunteer) {
        this.volunteer = volunteer;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MedicamentDTO{" +
                "medicament='" + medicament + '\'' +
                ", requirement=" + requirement +
                ", currentCount=" + currentCount +
                ", volunteer=" + volunteer +
                '}';
    }
}
