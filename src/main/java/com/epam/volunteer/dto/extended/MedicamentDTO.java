package com.epam.volunteer.dto.extended;

import com.epam.volunteer.dto.base.BaseMedicamentDTO;
import com.epam.volunteer.dto.base.BaseVolunteerDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "medicamentEx", parent = BaseMedicamentDTO.class, description = "Medicament that a volunteer needs.")
public class MedicamentDTO extends BaseMedicamentDTO {
    private BaseVolunteerDTO volunteerDTO;

    @ApiModelProperty(value = "Volunteer who needs medicament.")
    public BaseVolunteerDTO getVolunteerDTO() {
        return volunteerDTO;
    }

    public void setVolunteerDTO(BaseVolunteerDTO volunteerDTO) {
        this.volunteerDTO = volunteerDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MedicamentDTO)) return false;
        if (!super.equals(o)) return false;

        MedicamentDTO that = (MedicamentDTO) o;

        return volunteerDTO != null ? volunteerDTO.equals(that.volunteerDTO) : that.volunteerDTO == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (volunteerDTO != null ? volunteerDTO.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MedicamentDTO{ " + super.toString() +
                " volunteerDTO=" + volunteerDTO +
                '}';
    }
}
