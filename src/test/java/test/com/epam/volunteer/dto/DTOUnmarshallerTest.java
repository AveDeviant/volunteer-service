package test.com.epam.volunteer.dto;

import com.epam.volunteer.dto.AbstractDTO;
import com.epam.volunteer.dto.DTOType;
import com.epam.volunteer.dto.marshaller.DTOMarshaller;
import com.epam.volunteer.dto.marshaller.DTOUnmarshaller;
import com.epam.volunteer.entity.*;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;


public class DTOUnmarshallerTest {

    @Test
    public void unmarshallMedicamentEntity() {
        Medicament medicament = new Medicament();
        medicament.setId(1);
        medicament.setMedicament("test");
        AbstractDTO dto = DTOMarshaller.marshalDTO(medicament, DTOType.BASIC);
        AbstractEntity entity = DTOUnmarshaller.unmarshalDTO(dto);
        assert medicament.equals(entity);
    }

    @Test
    public void unmarshallVolunteerEntity() {
        Volunteer volunteer = new Volunteer();
        volunteer.setName("nikita");
        volunteer.setEmail("nikita");
        volunteer.setMedicament(new ArrayList<>());
        AbstractDTO dto = DTOMarshaller.marshalDTO(volunteer, DTOType.BASIC);
        AbstractEntity entity = DTOUnmarshaller.unmarshalDTO(dto);
        assert Volunteer.class.equals(entity.getClass());
        assert volunteer.equals(entity);
    }

    @Test
    public void unmarshallDonationEntity() {
        Donation donation = new Donation();
        donation.setTime(LocalDateTime.now());
        donation.setCount(5);
        Employee employee = new Employee();
        employee.setId(1);
        donation.setEmployee(employee);
        Medicament medicament = new Medicament();
        medicament.setId(3);
        donation.setMedicament(medicament);
        AbstractDTO dto = DTOMarshaller.marshalDTO(donation, DTOType.EXTENDED);
        AbstractEntity entity = DTOUnmarshaller.unmarshalDTO(dto);
        assert Donation.class.equals(entity.getClass());
        assert ((Donation)entity).getCount()==donation.getCount();
    }
}
