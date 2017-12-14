package com.epam.volunteer.dto;


import java.time.LocalDateTime;

public class DonationDTO extends AbstractDTO {
    private int count;
    private LocalDateTime time;
    private MedicamentDTO medicament;
    private EmployeeDTO employee;

   public DonationDTO() {}

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
}
