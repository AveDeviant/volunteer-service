package com.epam.volunteer.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "medicament")
public class Medicament implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_medicament")
    private long id;
    @Column(name = "medicament")
    private String medicament;
    @ManyToOne
    @JoinColumn(name = "id_volunteer")
    private Volunteer volunteer;
    @Column(name = "requirement")
    private int requirement;
    @Column(name = "current_count")
    private int currentCount;
    @Column(name = "status")
    private boolean status;

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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
