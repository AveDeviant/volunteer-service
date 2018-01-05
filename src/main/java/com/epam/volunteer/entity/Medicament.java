package com.epam.volunteer.entity;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "medicament")
@NamedQueries({
        @NamedQuery(name = "Medicament.getAll",
                query = "SELECT m FROM Medicament m"),
        @NamedQuery(name = "Medicament.countActual", query = "SELECT COUNT(m) FROM Medicament m WHERE m.isActual=1")
})
@Cacheable(value = false)
public class Medicament extends AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_medicament")
    private long id;

    @Column(name = "medicament")
    private String medicament;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_volunteer")
    private Volunteer volunteer;

    @Column(name = "requirement")
    private int requirement;

    @Column(name = "current_count")
    private int currentCount;

    @Column(name = "status")
    private boolean isActual;

    public Medicament() {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMedicament() {
        return medicament;
    }

    public void setMedicament(String medicament) {
        this.medicament = medicament;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }

    public int getRequirement() {
        return requirement;
    }

    public void setRequirement(int requirement) {
        this.requirement = requirement;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public boolean isActual() {
        return isActual;
    }

    public void setActual(boolean actual) {
        this.isActual = actual;
    }

    @Override
    public String toString() {
        return "Medicament{" +
                "id=" + id +
                ", medicament='" + medicament + '\'' +
                ", volunteer=" + volunteer +
                ", requirement=" + requirement +
                ", currentCount=" + currentCount +
                ", isActual=" + isActual +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Medicament that = (Medicament) o;

        if (id != that.id) return false;
        if (requirement != that.requirement) return false;
        if (currentCount != that.currentCount) return false;
        if (isActual != that.isActual) return false;
        if (medicament != null ? !medicament.equals(that.medicament) : that.medicament != null) return false;
        return volunteer != null ? volunteer.equals(that.volunteer) : that.volunteer == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (medicament != null ? medicament.hashCode() : 0);
        result = 31 * result + (volunteer != null ? volunteer.hashCode() : 0);
        result = 31 * result + requirement;
        result = 31 * result + currentCount;
        result = 31 * result + (isActual ? 1 : 0);
        return result;
    }
}
