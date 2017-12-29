package com.epam.volunteer.dao.impl;

import com.epam.volunteer.dao.EmployeeDAO;
import com.epam.volunteer.dao.exception.DAOException;
import com.epam.volunteer.entity.Employee;
import com.epam.volunteer.manager.EntityManagerWrapper;
import org.apache.logging.log4j.Level;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class EmployeeDAOImpl extends AbstractDAO implements EmployeeDAO {
    private EntityManager entityManager;

    @Inject
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Employee getByEmail(String email) throws DAOException {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
            Root<Employee> sm = criteriaQuery.from(Employee.class);
            criteriaQuery.where(criteriaBuilder.equal(sm.get("email"), email));
            TypedQuery<Employee> typedQuery = entityManager.createQuery(criteriaQuery);
            List<Employee> employeeList = typedQuery.getResultList();
            Employee employee = null;
            if (!employeeList.isEmpty()) {
                employee = employeeList.get(0);
            }
            return employee;
        } catch (Exception e) {
            getLogger().log(Level.ERROR, e.getMessage());
            throw new DAOException(e);
        }
    }

}
