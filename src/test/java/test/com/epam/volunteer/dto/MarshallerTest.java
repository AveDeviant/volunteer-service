package test.com.epam.volunteer.dto;

import com.epam.volunteer.dto.AbstractDTO;
import com.epam.volunteer.dto.DTOType;
import com.epam.volunteer.dto.base.BaseMedicamentDTO;
import com.epam.volunteer.dto.base.BaseVolunteerDTO;
import com.epam.volunteer.dto.extended.MedicamentDTO;
import com.epam.volunteer.dto.extended.VolunteerDTO;
import com.epam.volunteer.dto.marshaller.DTOMarshaller;
import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.entity.Volunteer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MarshallerTest {


    @Test
    public void testMarshalMedicamentEntity() {
        Medicament medicament = new Medicament();
        medicament.setMedicament("test");
        medicament.setId(1);
        medicament.setRequirement(10);
        AbstractDTO entity = DTOMarshaller.marshalDTO(medicament, DTOType.BASIC);
        assert entity.getClass().equals(BaseMedicamentDTO.class);
    }

    @Test
    public void testMarshalMedicamemtEntity2() {
        Medicament medicament = new Medicament();
        medicament.setMedicament("test");
        medicament.setId(1);
        medicament.setRequirement(10);
        AbstractDTO entity = DTOMarshaller.marshalDTO(medicament, DTOType.EXTENDED);
        assert entity.getClass().equals(MedicamentDTO.class);
    }

    @Test
    public void testMarshalVolunteerEntity() {
        Volunteer volunteer = new Volunteer();
        List<Medicament> medicamentList = new ArrayList<>();
        medicamentList.add(new Medicament());
        volunteer.setMedicament(medicamentList);
        volunteer.setName("Nikita");
        AbstractDTO entity = DTOMarshaller.marshalDTO(volunteer, DTOType.EXTENDED);
        assert entity.getClass().equals(VolunteerDTO.class);
        assert !((VolunteerDTO)entity).getMedicamentDTO().isEmpty();
    }

    @Test
    public void testMarshalVolunteerEntity2() {
        Volunteer volunteer = new Volunteer();
        List<Medicament> medicamentList = new ArrayList<>();
        medicamentList.add(new Medicament());
        volunteer.setMedicament(medicamentList);
        volunteer.setName("Nikita");
        AbstractDTO entity = DTOMarshaller.marshalDTO(volunteer, DTOType.BASIC);
        assert entity.getClass().equals(BaseVolunteerDTO.class);
    }
}
