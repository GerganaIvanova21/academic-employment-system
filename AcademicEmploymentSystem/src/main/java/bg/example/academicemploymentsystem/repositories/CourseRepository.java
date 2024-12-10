package bg.example.academicemploymentsystem.repositories;

import bg.example.academicemploymentsystem.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    Optional<Course> findCourseById(Integer id);

    List<Course> findCourseByCourseName(String name);

    List<Course> findCourseBySpecialityId(Integer specialityId);
}
