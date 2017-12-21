package com.epam.volunteer.dto.base;

import com.epam.volunteer.dto.AbstractDTO;

public class BaseDonationDTO extends AbstractDTO {
    private BaseEmployeeDTO employeeDTO;
    private BaseMedicamentDTO medicamentDTO;
    private int count;

    public BaseDonationDTO() {
    }

    public BaseEmployeeDTO getEmployeeDTO() {
        return employeeDTO;
    }

    public void setEmployeeDTO(BaseEmployeeDTO employeeDTO) {
        this.employeeDTO = employeeDTO;
    }

    public BaseMedicamentDTO getMedicamentDTO() {
        return medicamentDTO;
    }

    public void setMedicamentDTO(BaseMedicamentDTO medicamentDTO) {
        this.medicamentDTO = medicamentDTO;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseDonationDTO)) return false;
        if (!super.equals(o)) return false;

        BaseDonationDTO that = (BaseDonationDTO) o;
        if (getId() != that.getId()) return false;
        if (count != that.count) return false;
        if (employeeDTO != null ? !employeeDTO.equals(that.employeeDTO) : that.employeeDTO != null) return false;
        return medicamentDTO != null ? medicamentDTO.equals(that.medicamentDTO) : that.medicamentDTO == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (employeeDTO != null ? employeeDTO.hashCode() : 0);
        result = 31 * result + (medicamentDTO != null ? medicamentDTO.hashCode() : 0);
        result = 31 * result + count;
        return result;
    }

    @Override
    public String toString() {
        return "BaseDonationDTO{" +
                "employeeDTO=" + employeeDTO +
                ", medicamentDTO=" + medicamentDTO +
                ", count=" + count +
                '}';
    }
}
