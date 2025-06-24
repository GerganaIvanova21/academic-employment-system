package bg.example.academicemploymentsystem.services;

import bg.example.academicemploymentsystem.dto.request.DepartmentRequestDTO;
import bg.example.academicemploymentsystem.dto.response.DepartmentResponseDTO;
import bg.example.academicemploymentsystem.entities.Department;

import java.util.List;

public interface DepartmentService {
    List<Department> findAll();

    List<Department> findDepartmentByDepartmentName(String name);

    List<Department> findDepartmentByFacultyId(Long facultyId);

    //Търсене по част от името на катедрата (с игнориране на главни/малки букви)
    List<Department> findByDepartmentNameContainingIgnoreCase(String nameFragment);

    //Сортиране по азбучен ред
    List<Department> findAllByOrderByDepartmentNameAsc();

    // Сортиране в рамките на факултет
    List<DepartmentResponseDTO> findByFacultyIdOrderByDepartmentNameAsc(Long facultyId);

    DepartmentResponseDTO createDepartment(DepartmentRequestDTO requestDto);

    DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO requestDTO);
    void deleteDepartment(Long id);


}
