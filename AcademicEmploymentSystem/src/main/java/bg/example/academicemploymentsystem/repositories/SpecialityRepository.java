package bg.example.academicemploymentsystem.repositories;

import bg.example.academicemploymentsystem.entities.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface SpecialityRepository extends JpaRepository<Speciality, Integer> {
    Optional<Speciality> findSpecialitiesById(Integer id);

    List<Speciality> findSpecialitiesBySpecialityName(String name);
}
