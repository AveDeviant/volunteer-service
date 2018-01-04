package test.com.epam.volunteer.dto;

import com.epam.volunteer.dto.AbstractDTO;
import com.epam.volunteer.dto.DTOType;
import com.epam.volunteer.dto.base.BaseMedicamentDTO;
import com.epam.volunteer.dto.base.BaseVolunteerDTO;
import com.epam.volunteer.dto.extended.DonationDTO;
import com.epam.volunteer.dto.extended.MedicamentDTO;
import com.epam.volunteer.dto.extended.VolunteerDTO;
import com.epam.volunteer.dto.marshaller.DTOMarshaller;
import com.epam.volunteer.entity.Donation;
import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.entity.Volunteer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DTOMarshallerTest {


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
    public void testMarshalMedicamentEntity2() {
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
        assert !((VolunteerDTO) entity).getMedicament().isEmpty();
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

    @Test
    public void testMarshallDonationEntity() {
        DonationDTO donationDTO = new DonationDTO();
        donationDTO.setCount(2);
        donationDTO.setTime("");
        donationDTO.setMedicamentDTO(new MedicamentDTO());
        Donation donation = new Donation();
        donation.setCount(2);
        donation.setMedicament(new Medicament());
        AbstractDTO entity = DTOMarshaller.marshalDTO(donation, DTOType.EXTENDED);
        assert entity.getClass().equals(DonationDTO.class);
        assert entity.equals(donationDTO);
    }
}
