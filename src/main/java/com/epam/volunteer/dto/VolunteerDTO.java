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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VolunteerDTO that = (VolunteerDTO) o;
        if (getId()!=that.getId()) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        return medicament != null ? medicament.equals(that.medicament) : that.medicament == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (medicament != null ? medicament.hashCode() : 0);
        return result;
    }
}
