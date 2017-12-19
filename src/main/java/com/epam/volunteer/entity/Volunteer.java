package com.epam.volunteer.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "volunteer")
@NamedQueries({
        @NamedQuery(name = "Volunteer.getAll",
                query = "SELECT v FROM Volunteer v")
})
public class Volunteer extends AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_person")
    private long id;
    @Column(name = "email")
    private String email;
    @Column(name = "person_name")
    private String name;
    @OneToMany(mappedBy = "volunteer", fetch = FetchType.LAZY)
    private List<Medicament> medicament;

    public Volunteer() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Medicament> getMedicament() {
        return medicament;
    }

    public void setMedicament(List<Medicament> medicament) {
        this.medicament = medicament;
    }

    @Override
    public String toString() {
        return "Volunteer{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", medicament=" + medicament +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Volunteer volunteer = (Volunteer) o;
        if (id != volunteer.id) return false;
        if (email != null ? !email.equals(volunteer.email) : volunteer.email != null) return false;
        if (name != null ? !name.equals(volunteer.name) : volunteer.name != null) return false;
        if (medicament != null) {
            if (volunteer.medicament != null) {
                Object[] medicament1 = medicament.toArray();
                Object[] medicament2 = volunteer.medicament.toArray();
                return Arrays.equals(medicament1, medicament2);
            }
            return false;
        }
        return volunteer.medicament == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
