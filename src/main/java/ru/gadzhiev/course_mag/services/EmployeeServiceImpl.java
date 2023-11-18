package ru.gadzhiev.course_mag.services;

import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gadzhiev.course_mag.daos.EmployeeDao;
import ru.gadzhiev.course_mag.models.Employee;
import ru.gadzhiev.course_mag.models.Function;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private Jdbi jdbi;

    @Autowired
    private FunctionService functionService;

    @Override
    public Employee create(Employee employee) throws Exception {
        Function function = functionService.findById(employee.function());

        if(function == null)
            throw new IllegalArgumentException("Function does not exist: " + employee.function().id());

        Employee createdEmployee = jdbi.withExtension(EmployeeDao.class, extension -> extension.create(new Employee(
                employee.personnelNumber(),
                employee.firstName(),
                employee.lastName(),
                employee.middleName(),
                function,
                employee.email(),
                employee.phone(),
                employee.salary()
        )));
        return new Employee(
                createdEmployee.personnelNumber(),
                createdEmployee.firstName(),
                createdEmployee.lastName(),
                createdEmployee.middleName(),
                function,
                createdEmployee.email(),
                createdEmployee.phone(),
                createdEmployee.salary()
        );
    }

    @Override
    public Employee update(Employee employee) throws Exception {
        Function function = functionService.findById(employee.function());

        if(function == null)
            throw new IllegalArgumentException("Function does not exist: " + employee.function().id());

        Employee updatedEmployee = jdbi.withExtension(EmployeeDao.class, extension -> extension.update(new Employee(
                employee.personnelNumber(),
                employee.firstName(),
                employee.lastName(),
                employee.middleName(),
                function,
                employee.email(),
                employee.phone(),
                employee.salary()
        )));
        return new Employee(
                updatedEmployee.personnelNumber(),
                updatedEmployee.firstName(),
                updatedEmployee.lastName(),
                updatedEmployee.middleName(),
                function,
                updatedEmployee.email(),
                updatedEmployee.phone(),
                updatedEmployee.salary()
        );
    }

    @Override
    public int delete(Employee employee) throws Exception {
        int result = jdbi.withExtension(EmployeeDao.class, extension -> extension.delete(employee));
        if(result > 0)
            return result;
        throw new IllegalArgumentException("Employee does not exist");
    }

    @Override
    public List<Employee> findAllEmployees() throws Exception {
        return jdbi.withExtension(EmployeeDao.class, EmployeeDao::getAllEmployees);
    }

    @Override
    public Employee findById(Employee employee) throws Exception {
        Employee result = jdbi.withExtension(EmployeeDao.class, extension -> extension.getById(employee));
        if(result == null)
            throw new IllegalArgumentException("Employee does not exist: " + employee.personnelNumber());
        return result;
    }
}
