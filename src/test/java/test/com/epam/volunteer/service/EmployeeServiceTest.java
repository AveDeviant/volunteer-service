package test.com.epam.volunteer.service;

import com.epam.volunteer.dao.EmployeeDAO;
import com.epam.volunteer.dao.exception.DAOException;
import com.epam.volunteer.dao.impl.EmployeeDAOImpl;
import com.epam.volunteer.entity.Employee;
import com.epam.volunteer.service.exception.ServiceException;
import com.epam.volunteer.service.impl.EmployeeServiceImpl;
import org.junit.Test;
import org.mockito.Mockito;

public class EmployeeServiceTest {
    private static EmployeeServiceImpl employeeService = new EmployeeServiceImpl();
    private EmployeeDAO employeeDAO = Mockito.mock(EmployeeDAOImpl.class);


    @Test
    public void getByEmailTest() throws DAOException, ServiceException {
        employeeService.setEmployeeDAO(employeeDAO);
        Employee employee = new Employee();
        employee.setName("nikita");
        Mockito.when(employeeDAO.getByEmail("test@mail.com")).thenReturn(employee);
        assert employee.equals(employeeService.getByEmail("test@mail.com"));
    }

    @Test
    public void getByEmailTestDoesntExist() throws DAOException, ServiceException {
        employeeService.setEmployeeDAO(employeeDAO);
        Mockito.when(employeeDAO.getByEmail("test@mail.com")).thenReturn(null);
        assert null == (employeeService.getByEmail("test@mail.com"));
    }


}
