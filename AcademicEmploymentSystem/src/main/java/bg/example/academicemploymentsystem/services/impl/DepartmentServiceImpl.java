package bg.example.academicemploymentsystem.services.impl;


import bg.example.academicemploymentsystem.config.SecurityUtils;
import bg.example.academicemploymentsystem.dto.request.DepartmentRequestDTO;
import bg.example.academicemploymentsystem.dto.response.DepartmentResponseDTO;
import bg.example.academicemploymentsystem.entities.Department;
import bg.example.academicemploymentsystem.entities.Faculty;
import bg.example.academicemploymentsystem.exceptions.ResourceNotFoundException;
import bg.example.academicemploymentsystem.repositories.DepartmentRepository;
import bg.example.academicemploymentsystem.repositories.FacultyRepository;
import bg.example.academicemploymentsystem.services.DepartmentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final FacultyRepository facultyRepository;

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository, FacultyRepository facultyRepository) {
        this.departmentRepository = departmentRepository;
        this.facultyRepository = facultyRepository;
    }

    @Override
    public List<Department> findAll() {
        logger.info("Извличане на всички катедри.");
        List<Department> departments = departmentRepository.findAll();
        if (departments.isEmpty()) {
            logger.warn("Няма налични катедри в базата данни.");
        }
        return departments;
    }

    @Override
    public List<Department> findDepartmentByDepartmentName(String name) {
        logger.info("Търсене на катедра по име: {}", name);
        List<Department> departments = departmentRepository.findDepartmentByDepartmentName(name);
        if (departments.isEmpty()) {
            logger.warn("Не е намерена катедра с име: {}", name);
            throw new ResourceNotFoundException("Department", "departmentName", name);
        }
        return departments;
    }


    @Override
    public List<Department> findDepartmentByFacultyId(Long facultyId) {
        logger.info("Извличане на катедри по факултет с ID: {}", facultyId);
        List<Department> departments = departmentRepository.findDepartmentByFacultyId(facultyId);
        if (departments.isEmpty()) {
            logger.warn("Няма катедри към факултет с ID: {}", facultyId);
            throw new ResourceNotFoundException("Department", "facultyId", facultyId);
        }
        return departments;
    }

    @Override
    public List<Department> findByDepartmentNameContainingIgnoreCase(String departmentNameFragment) {
        logger.info("Търсене на катедра по име/ частично: {}", departmentNameFragment);
        List<Department> result = departmentRepository.findByDepartmentNameContainingIgnoreCase(departmentNameFragment);

        if (result.isEmpty()) {
            logger.warn("Не са намерени катедри, съдържащи: {}", departmentNameFragment);
            throw new ResourceNotFoundException("Department", "departmentNameFragment", departmentNameFragment);
        }
        return result;
    }

    @Override
    public List<Department> findAllByOrderByDepartmentNameAsc() {
        logger.info("Извличане на всички катедри, сортирани по азбучен ред.");

        List<Department> departments = departmentRepository.findAllByOrderByDepartmentNameAsc();

        if (departments.isEmpty()) {
            logger.warn("Няма налични катедри в системата.");
            throw new ResourceNotFoundException("Faculty", "all", "empty list");
        }

        return departments;
    }

    @Override
    public List<DepartmentResponseDTO> findByFacultyIdOrderByDepartmentNameAsc(Long facultyId) {
        logger.info("Търсене на катедри за факултет с ID: {}, сортирани по азбучен ред", facultyId);

        // Проверка дали факултетът съществува
        if (!facultyRepository.existsById(facultyId)) {
            logger.warn("Факултет с ID {} не съществува.", facultyId);
            throw new ResourceNotFoundException("Faculty", "id", facultyId);
        }

        List<Department> departments = departmentRepository.findByFacultyIdOrderByDepartmentNameAsc(facultyId);
        logger.info("Намерени {} катедри за факултет с ID {}", departments.size(), facultyId);

        return departments.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }


    private DepartmentResponseDTO mapToResponseDto(Department department) {
        DepartmentResponseDTO dto = new DepartmentResponseDTO();
        dto.setId(department.getId());
        dto.setDepartmentName(department.getDepartmentName());
        dto.setFacultyName(department.getFaculty().getFacultyName());
        return dto;
    }


    @Override
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO requestDto) {
        logger.info("Създаване на катедра с име: {}", requestDto.getDepartmentName());

        // Проверка за роля "Администратор"
        SecurityUtils.checkAdminAccess();

        // Проверка дали факултетът съществува
        Faculty faculty = facultyRepository.findById(requestDto.getFacultyId())
                .orElseThrow(() -> new ResourceNotFoundException("Faculty", "id", requestDto.getFacultyId()));

        // Проверка дали вече съществува катедра с това име във факултета
        if (departmentRepository.existsByDepartmentNameIgnoreCaseAndFacultyId(
                requestDto.getDepartmentName(), requestDto.getFacultyId())) {
            logger.warn("Катедра с име '{}' вече съществува във факултет с ID {}.",
                    requestDto.getDepartmentName(), requestDto.getFacultyId());
            throw new IllegalArgumentException("Катедра с такова име вече съществува във факултета.");
        }

        Department department = new Department();
        department.setDepartmentName(requestDto.getDepartmentName());
        department.setFaculty(faculty);

        Department saved = departmentRepository.save(department);
        logger.info("Катедра успешно създадена с ID: {}", saved.getId());

        return mapToResponseDto(saved);
    }

    @Override
    public DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO requestDTO) {
        logger.info("Редактиране на катедра с ID: {}", id);

        SecurityUtils.checkAdminAccess();

        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        Faculty faculty = facultyRepository.findById(requestDTO.getFacultyId())
                .orElseThrow(() -> new ResourceNotFoundException("Faculty", "id", requestDTO.getFacultyId()));

        // Проверка дали има друга катедра със същото име във факултета
        boolean nameConflict = departmentRepository
                .existsByDepartmentNameIgnoreCaseAndFacultyId(requestDTO.getDepartmentName(), requestDTO.getFacultyId());

        if (nameConflict && !existingDepartment.getDepartmentName().equalsIgnoreCase(requestDTO.getDepartmentName())) {
            logger.warn("Вече съществува катедра с име '{}' във факултет с ID {}.",
                    requestDTO.getDepartmentName(), requestDTO.getFacultyId());
            throw new IllegalArgumentException("Катедра с такова име вече съществува във факултета.");
        }

        // Обновяване на данните
        existingDepartment.setDepartmentName(requestDTO.getDepartmentName());
        existingDepartment.setFaculty(faculty);

        Department updated = departmentRepository.save(existingDepartment);
        logger.info("Катедрата с ID {} беше успешно редактирана.", updated.getId());

        return mapToResponseDto(updated);
    }

    @Override
    public void deleteDepartment(Long id) {
        logger.info("Изтриване на катедра по ID: {}", id);

        // Проверка за роля "Администратор"
        SecurityUtils.checkAdminAccess();

        // Проверка дали катедрата съществува
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        departmentRepository.delete(department);
        logger.info("Катедрата с ID {} беше успешно изтрита.", id);
    }











}


