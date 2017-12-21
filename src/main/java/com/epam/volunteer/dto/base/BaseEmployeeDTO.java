package com.epam.volunteer.dto.base;

import com.epam.volunteer.dto.AbstractDTO;

public class BaseEmployeeDTO extends AbstractDTO {
    private String email;
    private String name;

    public BaseEmployeeDTO() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEmployeeDTO)) return false;
        if (!super.equals(o)) return false;

        BaseEmployeeDTO that = (BaseEmployeeDTO) o;
        if (getId() != that.getId()) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
