package com.epam.volunteer.dto;

import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.entity.Volunteer;

import java.util.ArrayList;
import java.util.List;

public class DTOBuilder {


    public static AbstractDTO buildDTO(Object object) {
        if (object instanceof Medicament) {
            return buildMedicamentDTO((Medicament) object);
        }
        if (object instanceof Volunteer) {
            return buildVolunteerDTO((Volunteer) object);
        }
        return null;
    }


    public static List<AbstractDTO> buildDTOList(List<? extends Object> objects) {
        List<AbstractDTO> abstractDTOS = new ArrayList<>();
        for (Object object : objects) {
            abstractDTOS.add(buildDTO(object));
        }
        return abstractDTOS;
    }


    private static AbstractDTO buildMedicamentDTO(Medicament medicament) {
        MedicamentDTO medicamentDTO = new MedicamentDTO();
        medicamentDTO.setId(medicament.getId());
        medicamentDTO.setMedicament(medicament.getMedicament());
        medicamentDTO.setVolunteer((VolunteerDTO) buildVolunteerDTO(medicament.getVolunteer()));
        medicamentDTO.setRequirement(medicament.getRequirement());
        medicamentDTO.setCurrentCount(medicament.getCurrentCount());
        return medicamentDTO;
    }

    private static AbstractDTO buildVolunteerDTO(Volunteer volunteer) {
        VolunteerDTO volunteerDTO = new VolunteerDTO();
        volunteerDTO.setEmail(volunteer.getEmail());
        volunteerDTO.setName(volunteer.getName());
        volunteerDTO.setId(volunteer.getId());
        return volunteerDTO;
    }
}
