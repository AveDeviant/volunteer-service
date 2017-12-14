package com.epam.volunteer.dto;



import java.util.List;

public class VolunteerDTO extends AbstractDTO {
    private String name;
    private String email;

    private List<AbstractDTO> medicament;


    public VolunteerDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<AbstractDTO> getMedicament() {
        return medicament;
    }

    public void setMedicament(List<AbstractDTO> medicament) {
        this.medicament = medicament;
    }

    @Override
    public String toString() {
        return "VolunteerDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
