package bg.example.academicemploymentsystem.services.impl;


import bg.example.academicemploymentsystem.config.SecurityUtils;
import bg.example.academicemploymentsystem.dto.request.SpecialityRequestDTO;
import bg.example.academicemploymentsystem.dto.response.SpecialityResponseDTO;
import bg.example.academicemploymentsystem.entities.Speciality;
import bg.example.academicemploymentsystem.exceptions.ResourceNotFoundException;
import bg.example.academicemploymentsystem.repositories.SpecialityRepository;
import bg.example.academicemploymentsystem.services.SpecialityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialityServiceImpl implements SpecialityService {

    private final SpecialityRepository specialityRepository;
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    public SpecialityServiceImpl(SpecialityRepository specialityRepository) {
        this.specialityRepository = specialityRepository;
    }

    @Override
    public List<Speciality> findAll() {
        logger.info("Извличане на всички специалности.");
        List<Speciality> specialities = specialityRepository.findAll();
        if (specialities.isEmpty()) {
            logger.warn("Няма налични специалности в базата данни.");
        }
        return specialities;
    }

    @Override
    public List<Speciality> findSpecialityBySpecialityName(String name) {
        logger.info("Търсене на специалност по име: {}", name);
        List<Speciality> result = specialityRepository.findSpecialityBySpecialityName(name);
        if (result.isEmpty()) {
            logger.warn("Няма намерена специалност с име: {}", name);
            throw new ResourceNotFoundException("Speciality", "specialityName", name);
        }
        return result;
    }

    @Override
    public List<Speciality> findBySpecialityNameContainingIgnoreCase(String specialityNameFragment) {
        logger.info("Търсене на специалности по име/ частично: {}", specialityNameFragment);
        List<Speciality> result = specialityRepository.findBySpecialityNameContainingIgnoreCase(specialityNameFragment);

        if (result.isEmpty()) {
            logger.warn("Не са намерени специалности, съдържащи: {}", specialityNameFragment);
            throw new ResourceNotFoundException("Speciality", "specialityNameFragment", specialityNameFragment);
        }
        return result;
    }

    @Override
    public List<Speciality> findAllByOrderBySpecialityNameAsc() {
        logger.info("Извличане на всички специалности, сортирани по азбучен ред.");

        List<Speciality> specialities = specialityRepository.findAllByOrderBySpecialityNameAsc();

        if (specialities.isEmpty()) {
            logger.warn("Няма налични катедри в системата.");
            throw new ResourceNotFoundException("Speciality", "all", "empty list");
        }

        return specialities;
    }

    private SpecialityResponseDTO mapToResponseDto(Speciality speciality) {
        SpecialityResponseDTO dto = new SpecialityResponseDTO();
        dto.setId(speciality.getId());
        dto.setSpecialityName(speciality.getSpecialityName());
        return dto;
    }


    @Override
    public SpecialityResponseDTO createSpeciality(SpecialityRequestDTO requestDTO) {
        logger.info("Създаване на специалност с име: {}", requestDTO.getSpecialityName());

        SecurityUtils.checkAdminAccess();

        // Проверка за дублиране на име
        if (specialityRepository.existsBySpecialityNameIgnoreCase(requestDTO.getSpecialityName())) {
            logger.warn("Специалност с име '{}' вече съществува.", requestDTO.getSpecialityName());
            throw new IllegalArgumentException("Специалност с това име вече съществува.");
        }

        Speciality speciality = new Speciality();
        speciality.setSpecialityName(requestDTO.getSpecialityName());

        Speciality saved = specialityRepository.save(speciality);
        logger.info("Успешно създадена специалност с ID: {}", saved.getId());

        return mapToResponseDto(saved);
    }


    @Override
    public SpecialityResponseDTO updateSpeciality(Long id, SpecialityRequestDTO requestDto) {
        logger.info("Заявка за обновяване на специалност с ID: {}", id);

        // Проверка за роля "Администратор"
        SecurityUtils.checkAdminAccess();

        // Намиране на съществуваща специалност
        Speciality speciality = specialityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Speciality", "id", id));

        // Проверка дали вече съществува специалност с това име, но с различно ID
        boolean nameExistsForAnother = specialityRepository.existsBySpecialityNameIgnoreCaseAndIdNot(
                requestDto.getSpecialityName(), id);
        if (nameExistsForAnother) {
            logger.warn("Специалност с име '{}' вече съществува.", requestDto.getSpecialityName());
            throw new IllegalArgumentException("Специалност с това име вече съществува.");
        }

        speciality.setSpecialityName(requestDto.getSpecialityName());
        Speciality updated = specialityRepository.save(speciality);

        logger.info("Специалността с ID {} е успешно обновена.", updated.getId());

        return mapToResponseDto(updated);
    }


    @Override
    public void deleteSpeciality(Long id) {
        logger.info("Заявка за изтриване на специалност с ID: {}", id);

        // Проверка за роля "Администратор"
        SecurityUtils.checkAdminAccess();

        // Намиране на специалността
        Speciality speciality = specialityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Speciality", "id", id));

        specialityRepository.delete(speciality);
        logger.info("Специалност с ID {} е успешно изтрита.", id);
    }







}


