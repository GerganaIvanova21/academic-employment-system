package bg.example.academicemploymentsystem.services;

import bg.example.academicemploymentsystem.entities.Course;

 import java.util.List;

public interface CourseService {

    List<Course> findAll();

    List<Course> findCourseByCourseName(String name);

    List<Course> findCourseBySpecialityId(Long specialityId);

    //Търсене по част от името
    List<Course> findByCourseNameContainingIgnoreCase(String fragment);

    //Сортиране по име
    List<Course> findAllByOrderByCourseNameAsc();

    //Всички курсове за специалност, сортирани по име
    List<Course> findBySpecialityIdOrderByCourseNameAsc(Long specialityId);
}
