package bg.example.academicemploymentsystem.services.impl;


import bg.example.academicemploymentsystem.dto.request.CourseRequestDTO;
import bg.example.academicemploymentsystem.dto.response.CourseResponseDTO;
import bg.example.academicemploymentsystem.entities.Course;
import bg.example.academicemploymentsystem.entities.Speciality;
import bg.example.academicemploymentsystem.entities.User;
import bg.example.academicemploymentsystem.exceptions.ResourceNotFoundException;
import bg.example.academicemploymentsystem.repositories.AssignmentRepository;
import bg.example.academicemploymentsystem.repositories.CourseRepository;
import bg.example.academicemploymentsystem.repositories.SpecialityRepository;
import bg.example.academicemploymentsystem.repositories.UserRepository;
import bg.example.academicemploymentsystem.services.CourseService;
import bg.example.academicemploymentsystem.type.AssignmentType;
import bg.example.academicemploymentsystem.type.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final SpecialityRepository specialityRepository;
    private final AssignmentRepository assignmentRepository;

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository,
                             UserRepository userRepository,
                             SpecialityRepository specialityRepository,
                             AssignmentRepository assignmentRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.specialityRepository = specialityRepository;
        this.assignmentRepository = assignmentRepository;
    }

    @Override
    public List<Course> findAll() {
        logger.info("Извличане на всички курсове.");
        List<Course> courses = courseRepository.findAll();
        if (courses.isEmpty()) {
            logger.warn("Няма налични курсове в базата данни.");
            throw new ResourceNotFoundException("Course", "all", "empty list");
        }
        return courses;
    }

    @Override
    public List<Course> findCourseByCourseName(String name) {
        logger.info("Търсене на курсове по име: {}", name);
        List<Course> courses = courseRepository.findCourseByCourseName(name);
        if (courses.isEmpty()) {
            logger.warn("Не е намерен курс със следното име: {}", name);
            throw new ResourceNotFoundException("Course", "courseName", name);
        }
        return courses;
    }

    @Override
    public List<Course> findCourseBySpecialityId(Long specialityId) {
        logger.info("Извличане на курсове по ID на специалност: {}", specialityId);
        List<Course> courses = courseRepository.findCourseBySpecialityId(specialityId);
        if (courses.isEmpty()) {
            logger.warn("Няма намерени курсове за специалност с ID: {}", specialityId);
            throw new ResourceNotFoundException("Course", "specialityId", specialityId);
        }
        return courses;
    }

    @Override
    public List<Course> findByCourseNameContainingIgnoreCase(String courseNameFragment) {
        logger.info("Търсене на курсове по име/ частично: {}", courseNameFragment);
        List<Course> courses = courseRepository.findByCourseNameContainingIgnoreCase(courseNameFragment);
        if (courses.isEmpty()) {
            logger.warn("Няма намерени курсове със следното име: {}", courseNameFragment);
            throw new ResourceNotFoundException("Course", "courseNameFragment", courseNameFragment);
        }
        return courses;
    }

    @Override
    public List<Course> findAllByOrderByCourseNameAsc() {
        logger.info("Извличане на всички курсове, сортирани по име.");
        List<Course> courses = courseRepository.findAllByOrderByCourseNameAsc();
        if (courses.isEmpty()) {
            logger.warn("Няма курсове в системата.");
            throw new ResourceNotFoundException("Course", "sorted", "empty list");
        }
        return courses;
    }

    @Override
    public List<Course> findBySpecialityIdOrderByCourseNameAsc(Long specialityId) {
        logger.info("Извличане на курсове за специалност {} сортирани по име.", specialityId);
        List<Course> courses = courseRepository.findBySpecialityIdOrderByCourseNameAsc(specialityId);
        if (courses.isEmpty()) {
            logger.warn("Няма курсове за специалност с ID: {}", specialityId);
            throw new ResourceNotFoundException("Course", "specialityIdOrdered", specialityId);
        }
        return courses;
    }

    @Override
    public CourseResponseDTO createCourse(CourseRequestDTO requestDTO, String loggedInEmail) {
        logger.info("Опит за създаване на нов курс от потребител: {}", loggedInEmail);


        User user = userRepository.findByEmail(loggedInEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", loggedInEmail));


        if (!user.getRole().name().equals("TEACHER")) {
            logger.warn("Потребител {} няма право да създава курсове (не е преподавател).", loggedInEmail);
            throw new AccessDeniedException("Нямате право да създавате курсове, тъй като не сте преподавател.");
        }


        if (!hasTeachingPlanTaskForUser(user)) {
            logger.warn("Потребител {} няма възложена задача за планиране на обучение.", loggedInEmail);
            throw new AccessDeniedException("Нямате възложена задача за планиране на обучение.");
        }


        Speciality speciality = specialityRepository.findById(requestDTO.getSpecialityId())
                .orElseThrow(() -> new ResourceNotFoundException("Speciality", "id", requestDTO.getSpecialityId()));


        Course newCourse = new Course();
        newCourse.setCourseName(requestDTO.getCourseName());
        newCourse.setSemester(requestDTO.getSemester());
        newCourse.setSpeciality(speciality);
        newCourse.setTeacher(user);


        Course savedCourse = courseRepository.save(newCourse);
        logger.info("Успешно създаден курс с ID {} от потребител {}", savedCourse.getId(), loggedInEmail);


        CourseResponseDTO responseDTO = new CourseResponseDTO();
        responseDTO.setId(savedCourse.getId());
        responseDTO.setCourseName(savedCourse.getCourseName());
        responseDTO.setSemester(savedCourse.getSemester());
        responseDTO.setSpecialityName(speciality.getSpecialityName());
        responseDTO.setTeacherName(user.getFullName());

        return responseDTO;
    }

    private boolean hasTeachingPlanTaskForUser(User user) {
        return assignmentRepository.existsByAssignedToAndAssignmentTypeAndStatus(
                user,
                AssignmentType.TEACHING_PLAN,
                Status.PENDING // или Status.IN_PROGRESS ако приемаш и такива задачи
        );
    }

    @Override
    public CourseResponseDTO updateCourse(Long courseId, CourseRequestDTO requestDTO, String loggedInEmail) {
        logger.info("Обновяване на курс с ID: {} от потребител: {}", courseId, loggedInEmail);

        // Намери курса по ID
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));

        // Намери потребителя по email
        User user = userRepository.findByEmail(loggedInEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", loggedInEmail));

        // Проверка: дали потребителят е преподавателят, създал дисциплината
        if (course.getTeacher() == null || !course.getTeacher().getId().equals(user.getId())) {
            logger.warn("Потребителят {} няма права да обновява курс с ID {}", loggedInEmail, courseId);
            throw new AccessDeniedException("Нямате права да редактирате тази дисциплина.");
        }

        // Обнови данните
        course.setCourseName(requestDTO.getCourseName());
        course.setSemester(requestDTO.getSemester());

        if (requestDTO.getSpecialityId() != null) {
            Speciality speciality = specialityRepository.findById(requestDTO.getSpecialityId())
                    .orElseThrow(() -> new ResourceNotFoundException("Speciality", "id", requestDTO.getSpecialityId()));
            course.setSpeciality(speciality);
        }

        courseRepository.save(course);
        logger.info("Курс с ID {} беше успешно обновен от потребител {}", courseId, loggedInEmail);

        CourseResponseDTO responseDTO = new CourseResponseDTO();
        responseDTO.setId(course.getId());
        responseDTO.setCourseName(course.getCourseName());
        responseDTO.setSemester(course.getSemester());

        if (course.getSpeciality() != null) {
            responseDTO.setSpecialityName(course.getSpeciality().getSpecialityName());
        }
        if (course.getTeacher() != null) {
            responseDTO.setTeacherName(course.getTeacher().getFullName());
        }

        return responseDTO;
    }

    @Override
    public void deleteCourse(Long courseId, String loggedInEmail) {
        logger.info("Изтриване на курс с ID: {} от потребител: {}", courseId, loggedInEmail);

        //курс по ид
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));

      //потребителя по email
        User user = userRepository.findByEmail(loggedInEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", loggedInEmail));

        //дали потребителят е преподавателят, който е създал курса
        if (course.getTeacher() == null || !course.getTeacher().getId().equals(user.getId())) {
            logger.warn("Потребителят {} няма права да изтрие курс с ID {}", loggedInEmail, courseId);
            throw new AccessDeniedException("Нямате права да изтриете тази дисциплина.");
        }

        courseRepository.delete(course);
        logger.info("Курс с ID {} беше успешно изтрит от потребител {}", courseId, loggedInEmail);
    }
}





