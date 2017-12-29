package com.epam.volunteer.dto.base;

import com.epam.volunteer.dto.AbstractDTO;
import com.epam.volunteer.dto.extended.MedicamentDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "medicament", description = "Medicament that a volunteer needs.", subTypes = {MedicamentDTO.class})
public class BaseMedicamentDTO extends AbstractDTO {
    private String medicament;
    private int requirement;
    private int currentCount;
    private boolean isActual;

    @ApiModelProperty(value = "Medicament title.")
    public String getMedicament() {
        return medicament;
    }

    public void setMedicament(String medicament) {
        this.medicament = medicament;
    }

    @ApiModelProperty(value = "Medicament requirement.")
    public int getRequirement() {
        return requirement;
    }

    public void setRequirement(int requirement) {
        this.requirement = requirement;
    }

    @ApiModelProperty(value = "Number of medicament that already collected.")
    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    @ApiModelProperty(value = "Medicament actuality.")
    public boolean isActual() {
        return isActual;
    }

    public void setActual(boolean status) {
        this.isActual = status;
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
        if (isActual != that.isActual) return false;
        return medicament != null ? medicament.equals(that.medicament) : that.medicament == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (medicament != null ? medicament.hashCode() : 0);
        result = 31 * result + requirement;
        result = 31 * result + currentCount;
        result = 31 * result + (isActual ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BaseMedicamentDTO{" +
                "medicament='" + medicament + '\'' +
                ", requirement=" + requirement +
                ", currentCount=" + currentCount +
                ", status=" + isActual +
                '}';
    }
}
