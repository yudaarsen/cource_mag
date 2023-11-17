package ru.gadzhiev.course_mag.services;

import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gadzhiev.course_mag.daos.DepartmentDao;
import ru.gadzhiev.course_mag.models.Department;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private Jdbi jdbi;

    @Override
    public Department create(Department department) throws Exception {
        return jdbi.withExtension(DepartmentDao.class, extension -> extension.create(department));
    }

    @Override
    public Department update(Department department) throws Exception {
        Department updatedDepartment = jdbi.withExtension(DepartmentDao.class, extension -> extension.update(department));
        if(updatedDepartment == null)
            throw new IllegalArgumentException("Department does not exist: " + department.id());
        return updatedDepartment;
    }

    @Override
    public List<Department> findAll() throws Exception {
        return jdbi.withExtension(DepartmentDao.class, DepartmentDao::findAll);
    }

    @Override
    public int delete(Department department) throws Exception {
        int result = jdbi.withExtension(DepartmentDao.class, extension -> extension.delete(department));
        if(result > 0)
            return result;
        throw new IllegalArgumentException("Department does not exist: " + department.id());
    }
}
