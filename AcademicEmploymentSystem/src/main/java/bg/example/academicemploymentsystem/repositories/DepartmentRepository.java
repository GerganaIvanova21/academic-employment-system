package bg.example.academicemploymentsystem.repositories;

import bg.example.academicemploymentsystem.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Integer> {
    Optional<Department> findDepartmentById(Integer id);

    List<Department> findDepartmentByDepartmentName(String name);

    List<Department> findDepartmentByFacultyId(Integer facultyId);

    //List<Department> findDepartmentByUserId(Integer id);
}