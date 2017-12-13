package com.epam.volunteer.entity;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "donation")
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_person")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "id_medicament")
    private Medicament medicament;
    @Column(name = "count")
    private int count;

    @Column(name = "time")
    private LocalDateTime time;

    public Donation() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Medicament getMedicament() {
        return medicament;
    }

    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
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

    @Override
    public String toString() {
        return "Donation{" +
                "id=" + id +
                ", employee=" + employee +
                ", medicament=" + medicament +
                ", count=" + count +
                ", time=" + time +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Donation donation = (Donation) o;

        if (id != donation.id) return false;
        if (count != donation.count) return false;
        if (employee != null ? !employee.equals(donation.employee) : donation.employee != null) return false;
        if (medicament != null ? !medicament.equals(donation.medicament) : donation.medicament != null) return false;
        return time != null ? time.equals(donation.time) : donation.time == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (employee != null ? employee.hashCode() : 0);
        result = 31 * result + (medicament != null ? medicament.hashCode() : 0);
        result = 31 * result + count;
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }
}
