package com.epam.volunteer.dto.marshaller;

import com.epam.volunteer.dto.*;
import com.epam.volunteer.dto.base.BaseDonationDTO;
import com.epam.volunteer.dto.base.BaseEmployeeDTO;
import com.epam.volunteer.dto.base.BaseMedicamentDTO;
import com.epam.volunteer.dto.base.BaseVolunteerDTO;
import com.epam.volunteer.dto.extended.DonationDTO;
import com.epam.volunteer.dto.extended.EmployeeDTO;
import com.epam.volunteer.dto.extended.MedicamentDTO;
import com.epam.volunteer.dto.extended.VolunteerDTO;
import com.epam.volunteer.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DTOMarshaller {

    private DTOMarshaller() {
    }

    public static AbstractDTO marshalDTO(Object object, DTOType type) {
        if (object != null) {
            if (object instanceof Medicament) {
                return marshalMedicamentDTO((Medicament) object, type);
            }
            if (object instanceof Volunteer) {
                return marshalVolunteerDTO((Volunteer) object, type);
            }
            if (object instanceof Employee) {
                return marshalEmployeeDTO((Employee) object, type);
            }
            if (object instanceof Donation) {
                return marshalDonationDTO((Donation) object, type);
            }
        }
        return null;
    }


    public static List<AbstractDTO> marshalDTOList(List<? extends AbstractEntity> objects, DTOType type) {
        List<AbstractDTO> abstractDTOS = new ArrayList<>();
        if (objects != null) {
            for (Object object : objects) {
                abstractDTOS.add(marshalDTO(object, type));
            }
        }
        return abstractDTOS;
    }


    private static AbstractDTO marshalMedicamentDTO(Medicament medicament, DTOType type) {
        BaseMedicamentDTO medicamentDTO = null;
        if (type == DTOType.EXTENDED) {
            medicamentDTO = new MedicamentDTO();
            ((MedicamentDTO) medicamentDTO)
                    .setVolunteerDTO((BaseVolunteerDTO) marshalDTO(medicament.getVolunteer(), DTOType.BASIC));
        } else {
            medicamentDTO = new BaseMedicamentDTO();
        }
        medicamentDTO.setId(medicament.getId());
        medicamentDTO.setMedicament(medicament.getMedicament());
        medicamentDTO.setRequirement(medicament.getRequirement());
        medicamentDTO.setCurrentCount(medicament.getCurrentCount());
        medicamentDTO.setActual(medicament.isActual());
        return medicamentDTO;
    }

    private static AbstractDTO marshalVolunteerDTO(Volunteer volunteer, DTOType type) {
        BaseVolunteerDTO volunteerDTO = null;
        if (type == DTOType.EXTENDED) {
            volunteerDTO = new VolunteerDTO();
            List<BaseMedicamentDTO> medicamentDTOs = new ArrayList<>();
            if (Optional.ofNullable(volunteer.getMedicament()).isPresent()) {
                for (Medicament medicament : volunteer.getMedicament()) {
                    medicamentDTOs.add((BaseMedicamentDTO) marshalDTO(medicament, DTOType.BASIC));
                }
            }
            ((VolunteerDTO) volunteerDTO).
                    setMedicament(medicamentDTOs);
        } else {
            volunteerDTO = new BaseVolunteerDTO();
        }
        volunteerDTO.setEmail(volunteer.getEmail());
        volunteerDTO.setName(volunteer.getName());
        volunteerDTO.setId(volunteer.getId());
        return volunteerDTO;
    }

    private static AbstractDTO marshalEmployeeDTO(Employee employee, DTOType type) {
        BaseEmployeeDTO employeeDTO = null;
        if (type == DTOType.EXTENDED) {
            employeeDTO = new EmployeeDTO();
        } else {
            employeeDTO = new BaseEmployeeDTO();
        }
        employeeDTO.setId(employee.getId());
        employeeDTO.setEmail(employee.getEmail());
        employeeDTO.setName(employee.getName());
        return employeeDTO;
    }

    private static AbstractDTO marshalDonationDTO(Donation donation, DTOType type) {
        BaseDonationDTO donationDTO = null;
        if (type == DTOType.EXTENDED) {
            donationDTO = new DonationDTO();
            ((DonationDTO) donationDTO).setTime(donation.getTime());
            ((DonationDTO) donationDTO)
                    .setMedicamentDTO((BaseMedicamentDTO) marshalDTO(donation.getMedicament(), DTOType.BASIC));
            ((DonationDTO) donationDTO)
                    .setEmployeeDTO((BaseEmployeeDTO) marshalDTO(donation.getEmployee(), DTOType.BASIC));
        } else {
            donationDTO = new BaseDonationDTO();
        }
        donationDTO.setId(donation.getId());
        donationDTO.setCount(donation.getCount());
        return donationDTO;
    }
}
