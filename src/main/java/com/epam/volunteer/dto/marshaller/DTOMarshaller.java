package com.epam.volunteer.dto.marshaller;

import com.epam.volunteer.dto.*;
import com.epam.volunteer.entity.Donation;
import com.epam.volunteer.entity.Employee;
import com.epam.volunteer.entity.Medicament;
import com.epam.volunteer.entity.Volunteer;

import java.util.ArrayList;
import java.util.List;

public class DTOMarshaller {


    public static AbstractDTO marshalDTO(Object object, boolean withInternalObjects) {
        if (object != null) {
            if (object instanceof Medicament) {
                return marshalMedicamentDTO((Medicament) object, withInternalObjects);
            }
            if (object instanceof Volunteer) {
                return marshalVolunteerDTO((Volunteer) object, withInternalObjects);
            }
            if (object instanceof Employee) {
                return marshalEmployeeDTO((Employee) object, withInternalObjects);
            }
            if (object instanceof Donation) {
                return marshalDonationDTO((Donation) object, withInternalObjects);
            }
        }
        return null;
    }


    public static List<AbstractDTO> marshalDTOList(List<? extends Object> objects, boolean withInternalObjects) {
        List<AbstractDTO> abstractDTOS = new ArrayList<>();
        if (objects != null) {
            for (Object object : objects) {
                abstractDTOS.add(marshalDTO(object, withInternalObjects));
            }
        }
        return abstractDTOS;
    }


    private static AbstractDTO marshalMedicamentDTO(Medicament medicament, boolean withInternalObjects) {
        MedicamentDTO medicamentDTO = new MedicamentDTO();
        if (medicament != null) {
            medicamentDTO.setId(medicament.getId());
            medicamentDTO.setMedicament(medicament.getMedicament());
            medicamentDTO.setVolunteer((VolunteerDTO) marshalVolunteerDTO(medicament.getVolunteer(), withInternalObjects));
            medicamentDTO.setRequirement(medicament.getRequirement());
            medicamentDTO.setCurrentCount(medicament.getCurrentCount());
            medicamentDTO.setStatus(medicament.isStatus());
        }
        return medicamentDTO;
    }

    private static AbstractDTO marshalVolunteerDTO(Volunteer volunteer, boolean withInternalObjects) {
        VolunteerDTO volunteerDTO = new VolunteerDTO();
        if (volunteer != null) {
            volunteerDTO.setEmail(volunteer.getEmail());
            volunteerDTO.setName(volunteer.getName());
            volunteerDTO.setId(volunteer.getId());
            if (withInternalObjects) {
                List<AbstractDTO> medicamentDTOS = new ArrayList<>();
                medicamentDTOS.addAll(marshalDTOList(volunteer.getMedicament(), !withInternalObjects));
                volunteerDTO.setMedicament(medicamentDTOS);
            }
        }
        return volunteerDTO;
    }

    private static AbstractDTO marshalEmployeeDTO(Employee employee, boolean withInternalObjects) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setEmail(employee.getEmail());
        employeeDTO.setName(employee.getName());
        return employeeDTO;
    }

    private static AbstractDTO marshalDonationDTO(Donation donation, boolean withInternalObjects) {
        DonationDTO donationDTO = new DonationDTO();
        donationDTO.setId(donation.getId());
        donationDTO.setCount(donation.getCount());
        donationDTO.setMedicament((MedicamentDTO) marshalMedicamentDTO(donation.getMedicament(), !withInternalObjects));
        donationDTO.setEmployee((EmployeeDTO) marshalEmployeeDTO(donation.getEmployee(), withInternalObjects));
        donationDTO.setTime(donation.getTime());
        return donationDTO;
    }
}
