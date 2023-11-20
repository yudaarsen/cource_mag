package ru.gadzhiev.course_mag.services;

import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gadzhiev.course_mag.daos.DeductionDao;
import ru.gadzhiev.course_mag.daos.EmployeeDao;
import ru.gadzhiev.course_mag.daos.TimesheetDao;
import ru.gadzhiev.course_mag.models.*;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private Jdbi jdbi;

    @Autowired
    private FunctionService functionService;

    @Override
    public Employee create(final Employee employee) throws Exception {
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
    public Employee update(final Employee employee) throws Exception {
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
    public int delete(final Employee employee) throws Exception {
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
    public Employee findById(final Employee employee) throws Exception {
        Employee result = jdbi.withExtension(EmployeeDao.class, extension -> extension.getById(employee));
        if(result == null)
            throw new IllegalArgumentException("Employee does not exist: " + employee.personnelNumber());
        return result;
    }

    @Override
    public Deduction addEmployeeDeduction(final EmployeeDeduction employeeDeduction) throws Exception {
        int result = jdbi.withExtension(DeductionDao.class, extension -> extension.createEmployeeDeduction(employeeDeduction));
        if(result != 1)
            throw new IllegalStateException("Error during creating employee deduction");
        return jdbi.withExtension(DeductionDao.class, extension -> extension.getEmployeeDeduction(employeeDeduction));
    }

    @Override
    public int deleteEmployeeDeduction(final EmployeeDeduction employeeDeduction) throws Exception {
        int result = jdbi.withExtension(DeductionDao.class, extension -> extension.deleteEmployeeDeduction(employeeDeduction));
        if(result > 0)
            return result;
        throw new IllegalArgumentException("Employee deduction does not exist: "
                + employeeDeduction.employee().personnelNumber() + " " + employeeDeduction.deduction().code());
    }

    @Override
    public List<Deduction> findEmployeeDeductions(final Employee employee) throws Exception {
        return jdbi.withExtension(DeductionDao.class, extension -> extension.getEmployeeDeductions(employee));
    }

    @Override
    public Timesheet createTimesheet(final Timesheet timesheet) throws Exception {
        return jdbi.withExtension(TimesheetDao.class, extension -> extension.create(timesheet));
    }

    @Override
    public int deleteTimesheet(final Timesheet timesheet) throws Exception {
        int result = jdbi.withExtension(TimesheetDao.class, extension -> extension.delete(timesheet));
        if(result > 0)
            return result;
        throw new IllegalArgumentException("Timesheet does not exist " + timesheet.employee().personnelNumber() + " " + timesheet.year()
                    + " " + timesheet.month() + " " + timesheet.day());
    }

    @Override
    public List<Timesheet> findEmployeeTimesheet(final Employee employee, final int year, final int month) throws Exception {
        return jdbi.withExtension(TimesheetDao.class, extension -> extension.getEmployeeTimesheet(employee, year, month));
    }
}
