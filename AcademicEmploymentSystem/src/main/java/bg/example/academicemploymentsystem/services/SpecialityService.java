package bg.example.academicemploymentsystem.services;

import bg.example.academicemploymentsystem.dto.request.SpecialityRequestDTO;
import bg.example.academicemploymentsystem.dto.response.SpecialityResponseDTO;
import bg.example.academicemploymentsystem.entities.Speciality;

import java.util.List;

public interface SpecialityService {
    List<Speciality> findAll();

    List<Speciality> findSpecialityBySpecialityName(String name);

    //Търсене по част от име (ignore case)
    List<Speciality> findBySpecialityNameContainingIgnoreCase(String nameFragment);

    // Сортиране по азбучен ред
    List<Speciality> findAllByOrderBySpecialityNameAsc();

    SpecialityResponseDTO createSpeciality(SpecialityRequestDTO requestDTO);
    SpecialityResponseDTO updateSpeciality(Long id, SpecialityRequestDTO requestDto);
    void deleteSpeciality(Long id);



}
