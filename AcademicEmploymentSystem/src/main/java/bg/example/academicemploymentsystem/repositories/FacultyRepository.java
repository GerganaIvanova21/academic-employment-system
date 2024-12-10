package bg.example.academicemploymentsystem.repositories;

import bg.example.academicemploymentsystem.entities.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface FacultyRepository extends JpaRepository<Faculty, Integer> {
    Optional<Faculty> findFacultyById(Integer id);

    List<Faculty> findFacultyByFacultyName(String name);
}
