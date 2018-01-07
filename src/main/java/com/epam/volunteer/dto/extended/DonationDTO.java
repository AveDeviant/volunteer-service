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
    private BaseEmployeeDTO employee;
    private BaseMedicamentDTO medicament;

    @ApiModelProperty(value = "Donation confirmation time. Pattern: dd-MM-yyyy'T'HH:mm ")
    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @ApiModelProperty(value = "A company employee who made a donation.")
    public BaseEmployeeDTO getEmployee() {
        return employee;
    }


    public void setEmployeeDTO(BaseEmployeeDTO employeeDTO) {
        this.employee = employeeDTO;
    }

    @ApiModelProperty(value = "Desired medicament")
    public BaseMedicamentDTO getMedicament() {
        return medicament;
    }


    public void setMedicamentDTO(BaseMedicamentDTO medicamentDTO) {
        this.medicament = medicamentDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DonationDTO)) return false;
        if (!super.equals(o)) return false;

        DonationDTO that = (DonationDTO) o;

        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (employee != null ? !employee.equals(that.employee) : that.employee != null) return false;
        return medicament != null ? medicament.equals(that.medicament) : that.medicament == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (employee != null ? employee.hashCode() : 0);
        result = 31 * result + (medicament != null ? medicament.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DonationDTO{" +
                "time='" + time + '\'' +
                ", employee=" + employee +
                ", medicament=" + medicament +
                '}';
    }
}
