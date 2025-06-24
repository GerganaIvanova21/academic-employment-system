package bg.example.academicemploymentsystem.repositories;

import bg.example.academicemploymentsystem.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {
    //Optional<Department> findDepartmentById(Long id);

    List<Department> findDepartmentByDepartmentName(String name);

    List<Department> findDepartmentByFacultyId(Long facultyId);

    //Търсене по част от името на катедрата (с игнориране на главни/малки букви)
    List<Department> findByDepartmentNameContainingIgnoreCase(String departmentName);

    //Сортиране по азбучен ред
    List<Department> findAllByOrderByDepartmentNameAsc();

    // Сортиране в рамките на факултет
    List<Department> findByFacultyIdOrderByDepartmentNameAsc(Long facultyId);

    //Проверка за съществуване
    boolean existsByDepartmentNameIgnoreCase(String departmentName);

    boolean existsByDepartmentNameIgnoreCaseAndFacultyId(String name, Long facultyId);

    //Тъесене по име на катедра и факултет
    List<Department> findByFacultyIdAndDepartmentNameContainingIgnoreCase(Long facultyId, String departmentName);

    //Намиране на всички катедри, в които има определен потребител (по ID)
    @Query("SELECT d FROM Department d JOIN d.users u WHERE u.id = :userId")
    List<Department> findByUserId(Long userId);

}