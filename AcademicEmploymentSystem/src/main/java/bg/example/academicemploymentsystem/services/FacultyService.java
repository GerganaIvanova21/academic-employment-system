package bg.example.academicemploymentsystem.services;

import bg.example.academicemploymentsystem.dto.request.UpdateFacultyRequestDTO;
import bg.example.academicemploymentsystem.entities.Faculty;

import java.util.List;

public interface FacultyService {

    List<Faculty> findAll();

    List<Faculty> findFacultyByFacultyName(String name);

    List<Faculty> findByFacultyNameContainingIgnoreCase(String nameFragment);

    List<Faculty> findAllByOrderByFacultyNameAsc();

    Faculty createFaculty(Faculty faculty);

    Faculty updateFaculty(Long id, UpdateFacultyRequestDTO dto);

    void deleteFaculty(Long id);
}
