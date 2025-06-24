package bg.example.academicemploymentsystem.repositories;

import bg.example.academicemploymentsystem.entities.Course;
import bg.example.academicemploymentsystem.type.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    //Optional<Course> findCourseById(Long id);

    List<Course> findCourseByCourseName(String name);

    List<Course> findCourseBySemester(Semester semester);

    List<Course> findCourseBySpecialityId(Long specialityId);

    //Търсене по част от името
    List<Course> findByCourseNameContainingIgnoreCase(String courseName);

    //Сортиране по име
    List<Course> findAllByOrderByCourseNameAsc();

    //Всички курсове за специалност, сортирани по име
    List<Course> findBySpecialityIdOrderByCourseNameAsc(Long specialityId);

    //Проверка за съществуване
    boolean existsByCourseNameIgnoreCase(String courseName);

    //Търсене по име на курс и специалност
    List<Course> findByCourseNameContainingIgnoreCaseAndSpecialityId(String nameFragment, Long specialityId);





}
