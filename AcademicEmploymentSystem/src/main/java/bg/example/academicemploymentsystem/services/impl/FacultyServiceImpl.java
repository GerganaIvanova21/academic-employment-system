package bg.example.academicemploymentsystem.services.impl;


import bg.example.academicemploymentsystem.dto.request.UpdateFacultyRequestDTO;
import bg.example.academicemploymentsystem.entities.Faculty;
import bg.example.academicemploymentsystem.entities.User;
import bg.example.academicemploymentsystem.exceptions.ResourceNotFoundException;
import bg.example.academicemploymentsystem.repositories.FacultyRepository;
import bg.example.academicemploymentsystem.services.FacultyService;
import bg.example.academicemploymentsystem.type.Role;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public List<Faculty> findAll() {
        logger.info("Извличане на всички факултети.");
        List<Faculty> faculties = facultyRepository.findAll();
        if (faculties.isEmpty()) {
            logger.warn("Няма налични факултети в базата данни.");
        }
        return faculties;
    }

    @Override
    public List<Faculty> findFacultyByFacultyName(String name) {
        logger.info("Търсене на факултет по име: {}", name);
        List<Faculty> faculties = facultyRepository.findFacultyByFacultyName(name);
        if (faculties.isEmpty()) {
            logger.warn("Не е намерен факултет с име: {}", name);
        }
        return faculties;
    }

    @Override
    public List<Faculty> findByFacultyNameContainingIgnoreCase(String facultyNameFragment) {
        logger.info("Търсене на факултет по име/ частично: {}", facultyNameFragment);
        List<Faculty> result = facultyRepository.findByFacultyNameContainingIgnoreCase(facultyNameFragment);

        if (result.isEmpty()) {
            logger.warn("Не са намерени факултети, съдържащи: {}", facultyNameFragment);
            throw new ResourceNotFoundException("Faculty", "facultyNameFragment", facultyNameFragment);
        }

        return result;
    }

    @Override
    public List<Faculty> findAllByOrderByFacultyNameAsc() {
        logger.info("Извличане на всички факултети, сортирани по азбучен ред.");

        List<Faculty> faculties = facultyRepository.findAllByOrderByFacultyNameAsc();

        if (faculties.isEmpty()) {
            logger.warn("Няма налични факултети в системата.");
            throw new ResourceNotFoundException("Faculty", "all", "empty list");
        }

        return faculties;
    }

    @Override
    public Faculty createFaculty(Faculty faculty) {

        logger.info("Създашане на факултет от админстратор.");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Неавторизиран опит за създаване на факултет.");
            throw new SecurityException("Неоторизиран достъп.");
        }

        User currentUser = (User) authentication.getPrincipal();

        if (!currentUser.getRole().equals(Role.ADMIN)) {
            logger.warn("Потребителят с email {} няма право да създава факултети.", currentUser.getEmail());
            throw new SecurityException("Нямате права за създаване на факултет.");
        }

        if (facultyRepository.existsByFacultyNameIgnoreCase(faculty.getFacultyName())) {
            logger.warn("Факултет с име '{}' вече съществува.", faculty.getFacultyName());
            throw new IllegalArgumentException("Факултет с това име вече съществува.");
        }

        Faculty savedFaculty = facultyRepository.save(faculty);
        logger.info("Факултет '{}' беше създаден успешно от потребител с email: {}.",
                savedFaculty.getFacultyName(), currentUser.getEmail());

        return savedFaculty;
    }

    @Override
    @Transactional
    public Faculty updateFaculty(Long id, UpdateFacultyRequestDTO dto) {
        logger.info("Редактиране на факултет с ID {} към ново име '{}'", id, dto.getFacultyName());

        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty", "id", id));

        String newName = dto.getFacultyName().trim();

        if (facultyRepository.existsByFacultyNameIgnoreCase(newName)
                && !faculty.getFacultyName().equalsIgnoreCase(newName)) {
            logger.warn("Факултет с име '{}' вече съществува.", newName);
            throw new IllegalArgumentException("Факултет с такова име вече съществува.");
        }

        faculty.setFacultyName(newName);
        Faculty updated = facultyRepository.save(faculty);

        logger.info("Факултетът с ID {} беше успешно редактиран: '{}'.", id, newName);
        return updated;
    }

    @Override
    @Transactional
    public void deleteFaculty(Long id) {
        logger.info("Изтриване на факултет по ID: {}", id);

        // Проверка дали факултетът съществува
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty", "id", id));

        // Проверка за права на текущия потребител (само админ)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ADMIN"))) {
            logger.warn("Неоторизиран опит за изтриване на факултет от потребител: {}", auth != null ? auth.getName() : "анонимен");
            throw new SecurityException("Само администратор може да изтрива факултети.");
        }

        facultyRepository.delete(faculty);
        logger.info("Факултет с ID {} беше успешно изтрит.", id);
    }










}



