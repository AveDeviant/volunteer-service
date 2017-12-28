package com.epam.volunteer.dto.base;

import com.epam.volunteer.dto.AbstractDTO;

public class BaseMedicamentDTO extends AbstractDTO {
    private String medicament;
    private int requirement;
    private int currentCount;
    private boolean status;

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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseMedicamentDTO)) return false;
        if (!super.equals(o)) return false;

        BaseMedicamentDTO that = (BaseMedicamentDTO) o;
        if (getId() != that.getId()) return false;
        if (requirement != that.requirement) return false;
        if (currentCount != that.currentCount) return false;
        if (status != that.status) return false;
        return medicament != null ? medicament.equals(that.medicament) : that.medicament == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (medicament != null ? medicament.hashCode() : 0);
        result = 31 * result + requirement;
        result = 31 * result + currentCount;
        result = 31 * result + (status ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BaseMedicamentDTO{" +
                "medicament='" + medicament + '\'' +
                ", requirement=" + requirement +
                ", currentCount=" + currentCount +
                ", status=" + status +
                '}';
    }
}
