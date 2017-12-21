package com.epam.volunteer.dto.extended;

import com.epam.volunteer.dto.base.BaseDonationDTO;

import java.time.LocalDateTime;

public class DonationDTO extends BaseDonationDTO {
    private LocalDateTime time;

    public DonationDTO() {
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DonationDTO)) return false;
        if (!super.equals(o)) return false;

        DonationDTO that = (DonationDTO) o;

        return time != null ? time.equals(that.time) : that.time == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DonationDTO{ " + super.toString() +
                " time=" + time +
                '}';
    }
}
