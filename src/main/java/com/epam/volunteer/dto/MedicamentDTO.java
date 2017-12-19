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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MedicamentDTO that = (MedicamentDTO) o;
        if (getId() != that.getId()) return false;
        if (requirement != that.requirement) return false;
        if (currentCount != that.currentCount) return false;
        if (status != that.status) return false;
        if (medicament != null ? !medicament.equals(that.medicament) : that.medicament != null) return false;
        return volunteer != null ? volunteer.equals(that.volunteer) : that.volunteer == null;
    }

    @Override
    public int hashCode() {
        int result = medicament != null ? medicament.hashCode() : 0;
        result = 31 * result + requirement;
        result = 31 * result + currentCount;
        result = 31 * result + (volunteer != null ? volunteer.hashCode() : 0);
        result = 31 * result + (status ? 1 : 0);
        return result;
    }
}
