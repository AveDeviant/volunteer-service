package com.epam.volunteer.dto.base;

import com.epam.volunteer.dto.AbstractDTO;

public class BaseVolunteerDTO extends AbstractDTO {
    private String name;
    private String email;

    public BaseVolunteerDTO() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseVolunteerDTO)) return false;
        if (!super.equals(o)) return false;

        BaseVolunteerDTO that = (BaseVolunteerDTO) o;
        if (getId()!=that.getId()) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return email != null ? email.equals(that.email) : that.email == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BaseVolunteerDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
