package com.epam.volunteer.dto.marshaller;

import com.epam.volunteer.dto.*;
import com.epam.volunteer.entity.*;

import java.util.ArrayList;
import java.util.List;

public class DTOUnmarshaller {


    public static AbstractEntity unmarshalDTO(AbstractDTO dto) {
        if (dto instanceof MedicamentDTO) {
            return unmarshalMedicament((MedicamentDTO) dto);
        }
        if (dto instanceof VolunteerDTO) {
            return unmarshalVolunteer((VolunteerDTO) dto);
        }
        if (dto instanceof EmployeeDTO) {
            return unmarshalEmployee((EmployeeDTO) dto);
        }
        if (dto instanceof DonationDTO) {
            return unmarshallDonation((DonationDTO) dto);
        }
        return null;
    }


    private static Medicament unmarshalMedicament(MedicamentDTO dto) {
        Medicament medicament = new Medicament();
        medicament.setId(dto.getId());
        medicament.setVolunteer((Volunteer) unmarshalDTO(dto.getVolunteer()));
        medicament.setMedicament(dto.getMedicament());
        medicament.setCurrentCount(dto.getCurrentCount());
        medicament.setRequirement(dto.getRequirement());
        medicament.setStatus(dto.isStatus());
        return medicament;
    }

    private static Volunteer unmarshalVolunteer(VolunteerDTO dto) {
        Volunteer volunteer = new Volunteer();
        volunteer.setId(dto.getId());
        volunteer.setEmail(dto.getEmail());
        volunteer.setName(dto.getName());
        List<Medicament> medicamentList = new ArrayList<>();
        for (AbstractDTO medicamentDTO : dto.getMedicament()) {
            medicamentList.add((Medicament) unmarshalDTO(medicamentDTO));
        }
        volunteer.setMedicament(medicamentList);
        return volunteer;
    }

    private static Employee unmarshalEmployee(EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setId(dto.getId());
        employee.setEmail(dto.getEmail());
        employee.setName(dto.getName());
        return employee;
    }

    private static Donation unmarshallDonation(DonationDTO dto) {
        Donation donation = new Donation();
        donation.setId(dto.getId());
        donation.setCount(dto.getCount());
        donation.setEmployee(unmarshalEmployee(dto.getEmployee()));
        donation.setMedicament(unmarshalMedicament(dto.getMedicament()));
        return donation;
    }

}
