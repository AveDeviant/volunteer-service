package com.epam.volunteer.dto.base;

import com.epam.volunteer.dto.AbstractDTO;
import com.epam.volunteer.dto.extended.DonationDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "donation", description = "Donation to volunteers from a company employee.", subTypes = {DonationDTO.class})
public class BaseDonationDTO extends AbstractDTO {
    private int count;

    @ApiModelProperty(value = "Donation amount")
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
