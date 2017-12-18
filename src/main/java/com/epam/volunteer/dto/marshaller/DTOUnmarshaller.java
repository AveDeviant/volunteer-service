package com.epam.volunteer.dto.marshaller;

import com.epam.volunteer.dto.*;
import com.epam.volunteer.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DTOUnmarshaller {


    public static AbstractEntity unmarshalDTO(AbstractDTO dto) {
        if (dto != null) {
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
                return unmarshalDonation((DonationDTO) dto);
            }
        }
        return null;
    }


    private static Medicament unmarshalMedicament(MedicamentDTO dto) {
        if (dto == null) {
            return null;
        }
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
        if (dto == null) {
            return null;
        }
        Volunteer volunteer = new Volunteer();
        volunteer.setId(dto.getId());
        volunteer.setEmail(dto.getEmail());
        volunteer.setName(dto.getName());
        List<Medicament> medicamentList = new ArrayList<>();
        if (Optional.ofNullable(dto.getMedicament()).isPresent()) {
            dto.getMedicament().stream().forEach(m -> medicamentList.add((Medicament) unmarshalDTO(m)));
        }
        volunteer.setMedicament(medicamentList);
        return volunteer;
    }

    private static Employee unmarshalEmployee(EmployeeDTO dto) {
        if (dto == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(dto.getId());
        employee.setEmail(dto.getEmail());
        employee.setName(dto.getName());
        return employee;
    }

    private static Donation unmarshalDonation(DonationDTO dto) {
        if (dto == null) {
            return null;
        }
        Donation donation = new Donation();
        donation.setId(dto.getId());
        donation.setCount(dto.getCount());
        donation.setEmployee(unmarshalEmployee(dto.getEmployee()));
        donation.setMedicament(unmarshalMedicament(dto.getMedicament()));
        return donation;
    }

}
