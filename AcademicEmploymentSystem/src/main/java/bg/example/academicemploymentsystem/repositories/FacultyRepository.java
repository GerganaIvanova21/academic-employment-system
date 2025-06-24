package bg.example.academicemploymentsystem.repositories;

import bg.example.academicemploymentsystem.entities.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    List<Faculty> findFacultyByFacultyName(String name);


    //Търсене по част от името (case insensitive)
    List<Faculty> findByFacultyNameContainingIgnoreCase(String facultyName);

    //Сортиране по азбучен ред
    List<Faculty> findAllByOrderByFacultyNameAsc();

    //Проверка за съществуване
    boolean existsByFacultyNameIgnoreCase(String facultyName);



}
