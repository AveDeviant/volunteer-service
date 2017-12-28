package com.epam.volunteer.dto.base;

import com.epam.volunteer.dto.AbstractDTO;

public class BaseDonationDTO extends AbstractDTO {
    private int count;

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

        return count == that.count;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + count;
        return result;
    }
}
