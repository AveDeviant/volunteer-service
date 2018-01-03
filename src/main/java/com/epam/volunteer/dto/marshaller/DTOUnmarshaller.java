package com.epam.volunteer.dto.marshaller;

import com.epam.volunteer.dto.*;
import com.epam.volunteer.dto.base.BaseDonationDTO;
import com.epam.volunteer.dto.base.BaseEmployeeDTO;
import com.epam.volunteer.dto.base.BaseMedicamentDTO;
import com.epam.volunteer.dto.base.BaseVolunteerDTO;
import com.epam.volunteer.dto.extended.MedicamentDTO;
import com.epam.volunteer.dto.extended.VolunteerDTO;
import com.epam.volunteer.entity.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DTOUnmarshaller {

    private DTOUnmarshaller() {
    }

    public static AbstractEntity unmarshalDTO(AbstractDTO dto) {
        if (dto != null) {
            if (dto instanceof BaseMedicamentDTO) {
                return unmarshalMedicament((BaseMedicamentDTO) dto);
            }
            if (dto instanceof BaseVolunteerDTO) {
                return unmarshalVolunteer((BaseVolunteerDTO) dto);
            }
            if (dto instanceof BaseEmployeeDTO) {
                return unmarshalEmployee((BaseEmployeeDTO) dto);
            }
            if (dto instanceof BaseDonationDTO) {
                return unmarshalDonation((BaseDonationDTO) dto);
            }
        }
        return null;
    }


    private static Medicament unmarshalMedicament(BaseMedicamentDTO dto) {
        if (dto == null) {
            return null;
        }
        Medicament medicament = new Medicament();
        medicament.setId(dto.getId());
        if (dto.getClass() == MedicamentDTO.class) {
            medicament.setVolunteer((Volunteer) unmarshalDTO(((MedicamentDTO) dto).getVolunteer()));
        }
        medicament.setMedicament(dto.getMedicament());
        medicament.setCurrentCount(dto.getCurrentCount());
        medicament.setRequirement(dto.getRequirement());
        medicament.setActual(dto.isActual());
        return medicament;
    }

    private static Volunteer unmarshalVolunteer(BaseVolunteerDTO dto) {
        if (dto == null) {
            return null;
        }
        Volunteer volunteer = new Volunteer();
        volunteer.setId(dto.getId());
        volunteer.setEmail(dto.getEmail());
        volunteer.setName(dto.getName());
        List<Medicament> medicamentList = new ArrayList<>();
        if (dto.getClass() == VolunteerDTO.class) {
            List<BaseMedicamentDTO> medicamentDTOs = ((VolunteerDTO) dto).getMedicament();
            if (Optional.ofNullable(medicamentDTOs).isPresent()) {
                medicamentDTOs.stream().forEach(m -> medicamentList.add((Medicament) unmarshalDTO(m)));
            }
        }
        volunteer.setMedicament(medicamentList);
        return volunteer;
    }

    private static Employee unmarshalEmployee(BaseEmployeeDTO dto) {
        if (dto == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(dto.getId());
        employee.setEmail(dto.getEmail());
        employee.setName(dto.getName());
        return employee;
    }

    private static Donation unmarshalDonation(BaseDonationDTO dto) {
        if (dto == null) {
            return null;
        }
        Donation donation = new Donation();
        try {
            donation.setId(dto.getId());
            donation.setCount(dto.getCount());
        } catch (Exception e) {
            LogManager.getLogger().log(Level.ERROR, e.getMessage());
        }
        return donation;
    }

}
