package bg.example.academicemploymentsystem.repositories;

import bg.example.academicemploymentsystem.entities.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SpecialityRepository extends JpaRepository<Speciality, Long> {
    //Optional<Speciality> findSpecialityById(Long id);

    List<Speciality> findSpecialityBySpecialityName(String name);

    //Търсене по част от име (ignore case)
    List<Speciality> findBySpecialityNameContainingIgnoreCase(String specialityName);

    // Сортиране по азбучен ред
    List<Speciality> findAllByOrderBySpecialityNameAsc();

    boolean existsBySpecialityNameIgnoreCase(String name);




}
