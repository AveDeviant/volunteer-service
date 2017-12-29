package com.epam.volunteer.dto.extended;

import com.epam.volunteer.dto.base.BaseDonationDTO;
import com.epam.volunteer.dto.base.BaseEmployeeDTO;
import com.epam.volunteer.dto.base.BaseMedicamentDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@ApiModel(value = "donationEx", parent = BaseDonationDTO.class, description = "Donation to volunteers from a company employee")
public class DonationDTO extends BaseDonationDTO {
    private LocalDateTime time;
    private BaseEmployeeDTO employeeDTO;
    private BaseMedicamentDTO medicamentDTO;

    @ApiModelProperty(value = "Donation confirmation time.")
    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @ApiModelProperty(value = "A company employee who made a donation.")
    public BaseEmployeeDTO getEmployeeDTO() {
        return employeeDTO;
    }


    public void setEmployeeDTO(BaseEmployeeDTO employeeDTO) {
        this.employeeDTO = employeeDTO;
    }

    @ApiModelProperty(value = "Desired medicament")
    public BaseMedicamentDTO getMedicamentDTO() {
        return medicamentDTO;
    }


    public void setMedicamentDTO(BaseMedicamentDTO medicamentDTO) {
        this.medicamentDTO = medicamentDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DonationDTO)) return false;
        if (!super.equals(o)) return false;

        DonationDTO that = (DonationDTO) o;

        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (employeeDTO != null ? !employeeDTO.equals(that.employeeDTO) : that.employeeDTO != null) return false;
        return medicamentDTO != null ? medicamentDTO.equals(that.medicamentDTO) : that.medicamentDTO == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (employeeDTO != null ? employeeDTO.hashCode() : 0);
        result = 31 * result + (medicamentDTO != null ? medicamentDTO.hashCode() : 0);
        return result;
    }
}
