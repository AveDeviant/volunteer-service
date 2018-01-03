package com.epam.volunteer.dto.extended;

import com.epam.volunteer.dto.base.BaseMedicamentDTO;
import com.epam.volunteer.dto.base.BaseVolunteerDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "volunteerEx", parent = BaseVolunteerDTO.class)
public class VolunteerDTO extends BaseVolunteerDTO {
    private List<BaseMedicamentDTO> medicament;

    @ApiModelProperty(value = "List of medicament")
    public List<BaseMedicamentDTO> getMedicament() {
        return medicament;
    }

    public void setMedicament(List<BaseMedicamentDTO> medicamentDTOs) {
        this.medicament = medicamentDTOs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VolunteerDTO)) return false;
        if (!super.equals(o)) return false;

        VolunteerDTO that = (VolunteerDTO) o;

        return medicament != null ? medicament.equals(that.medicament) : that.medicament == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (medicament != null ? medicament.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "VolunteerDTO{ " + super.toString() +
                " medicamentDTOs=" + medicament +
                '}';
    }
}
