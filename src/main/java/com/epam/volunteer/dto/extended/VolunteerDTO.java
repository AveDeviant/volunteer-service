package com.epam.volunteer.dto.extended;

import com.epam.volunteer.dto.base.BaseMedicamentDTO;
import com.epam.volunteer.dto.base.BaseVolunteerDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "volunteerEx", parent = BaseVolunteerDTO.class)
public class VolunteerDTO extends BaseVolunteerDTO {
    private List<BaseMedicamentDTO> medicamentDTOs;

    @ApiModelProperty(value = "List of medicament")
    public List<BaseMedicamentDTO> getMedicamentDTO() {
        return medicamentDTOs;
    }

    public void setMedicamentDTO(List<BaseMedicamentDTO> medicamentDTOs) {
        this.medicamentDTOs = medicamentDTOs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VolunteerDTO)) return false;
        if (!super.equals(o)) return false;

        VolunteerDTO that = (VolunteerDTO) o;

        return medicamentDTOs != null ? medicamentDTOs.equals(that.medicamentDTOs) : that.medicamentDTOs == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (medicamentDTOs != null ? medicamentDTOs.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "VolunteerDTO{ " + super.toString() +
                " medicamentDTOs=" + medicamentDTOs +
                '}';
    }
}
