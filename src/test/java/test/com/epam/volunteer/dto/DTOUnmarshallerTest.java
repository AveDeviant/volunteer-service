package test.com.epam.volunteer.dto;

import com.epam.volunteer.dto.AbstractDTO;
import com.epam.volunteer.dto.DTOType;
import com.epam.volunteer.dto.marshaller.DTOMarshaller;
import com.epam.volunteer.dto.marshaller.DTOUnmarshaller;
import com.epam.volunteer.entity.AbstractEntity;
import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.entity.Volunteer;
import org.junit.Test;

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
        System.out.println(entity);
        assert Volunteer.class.equals(entity.getClass());
        assert volunteer.equals(entity);
    }
}
