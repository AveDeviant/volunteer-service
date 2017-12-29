package com.epam.volunteer.dto.extended;

import com.epam.volunteer.dto.base.BaseEmployeeDTO;
import io.swagger.annotations.ApiModel;

@ApiModel(value = "employeeEx", parent = BaseEmployeeDTO.class)
public class EmployeeDTO extends BaseEmployeeDTO {

}
