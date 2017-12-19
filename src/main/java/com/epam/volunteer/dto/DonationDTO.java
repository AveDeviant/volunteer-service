package com.epam.volunteer.dto;


import java.time.LocalDateTime;

public class DonationDTO extends AbstractDTO {
    private int count;
    private LocalDateTime time;
    private MedicamentDTO medicament;
    private EmployeeDTO employee;

    public DonationDTO() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public MedicamentDTO getMedicament() {
        return medicament;
    }

    public void setMedicament(MedicamentDTO medicament) {
        this.medicament = medicament;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DonationDTO)) return false;
        if (!super.equals(o)) return false;

        DonationDTO that = (DonationDTO) o;
        if (getId() != that.getId()) return false;
        if (count != that.count) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (medicament != null ? !medicament.equals(that.medicament) : that.medicament != null) return false;
        return employee != null ? employee.equals(that.employee) : that.employee == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + count;
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (medicament != null ? medicament.hashCode() : 0);
        result = 31 * result + (employee != null ? employee.hashCode() : 0);
        return result;
    }
}
