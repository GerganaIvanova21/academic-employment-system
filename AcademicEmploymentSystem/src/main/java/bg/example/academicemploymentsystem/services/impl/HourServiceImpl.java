package bg.example.academicemploymentsystem.services.impl;


import bg.example.academicemploymentsystem.entities.Hour;
import bg.example.academicemploymentsystem.exceptions.ResourceNotFoundException;
import bg.example.academicemploymentsystem.repositories.CourseRepository;
import bg.example.academicemploymentsystem.repositories.HourRepository;
import bg.example.academicemploymentsystem.repositories.UserRepository;
import bg.example.academicemploymentsystem.services.HourService;
import bg.example.academicemploymentsystem.type.HourType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HourServiceImpl implements HourService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    private final HourRepository hourRepository;
    private final CourseRepository courseRepository;

    private final UserRepository userRepository;


    @Autowired
    public HourServiceImpl(HourRepository hourRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.hourRepository = hourRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Hour> findAll() {
        logger.info("Извличане на всички часове.");
        List<Hour> hours = hourRepository.findAll();
        if (hours.isEmpty()) {
            logger.warn("Няма налични часове в базата данни.");
            throw new ResourceNotFoundException("Hour", "all", "empty list");
        }
        return hours;
    }

    @Override
    public List<Hour> findHourByCourseId(Long courseId) {
        logger.info("Извличане на часове за курс с ID: {}", courseId);

        if (!courseRepository.existsById(courseId)) {
            logger.warn("Курс с ID {} не е намерен", courseId);
            throw new ResourceNotFoundException("Course", "id", "not found");
        }

        //Извличане на часовете за курса, подредени по ден и начален час
        List<Hour> hours = hourRepository.findByCourseIdOrderByDayAscStartTimeAsc(courseId);

        logger.info("Намерени са {} часа за курса с ID {}", hours.size(), courseId);

        return hours;
    }

    @Override
    public List<Hour> findHourByUserId(Long userId) {
        logger.info("Извличане на часове за потребител с ID: {}", userId);

        // Проверка дали потребителят съществува
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        List<Hour> hours = hourRepository.findHourByUserId(userId);
        if (hours.isEmpty()) {
            throw new ResourceNotFoundException("Hour", "userId", userId);
        }

        return hours;
    }

    @Override
    public List<Hour> findByUserIdAndDay(Long userId, LocalDate day) {
        logger.info("Извличане на часове за потребител с ID {} в деня {}", userId, day);

        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        List<Hour> hours = hourRepository.findByUserIdAndDay(userId, day);
        if (hours.isEmpty()) {
            throw new ResourceNotFoundException("Hour", "userId and day", userId + " и " + day);
        }

        return hours;
    }

    @Override
    public List<Hour> findByUserIdOrderByDayAscStartTimeAsc(Long userId) {
        logger.info("Извличане на всички часове за потребител с ID {} (подредени по ден и начален час)", userId);

        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        List<Hour> hours = hourRepository.findByUserIdOrderByDayAscStartTimeAsc(userId);
        if (hours.isEmpty()) {
            throw new ResourceNotFoundException("Hour", "userId", userId);
        }

        return hours;
    }

    @Override
    public List<Hour> findByCourseIdAndDay(Long courseId, LocalDate day) {
        logger.info("Извличане на часове за курс с ID {} за дата {}", courseId, day);

        courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));

        List<Hour> hours = hourRepository.findByCourseIdAndDay(courseId, day);
        if (hours.isEmpty()) {
            throw new ResourceNotFoundException("Hour", "courseId and day", courseId + " и " + day);
        }

        return hours;
    }

    @Override
    public List<Hour> findByCourseIdOrderByDayAscStartTimeAsc(Long courseId) {
        logger.info("Извличане на часове за курс с ID {} (подредени по дата и начален час)", courseId);

        courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));

        List<Hour> hours = hourRepository.findByCourseIdOrderByDayAscStartTimeAsc(courseId);
        if (hours.isEmpty()) {
            throw new ResourceNotFoundException("Hour", "courseId", courseId);
        }

        return hours;
    }

    @Override
    public List<Hour> findByRoomAndDay(String room, LocalDate day) {
        logger.info("Извличане на часове в зала {} за дата {}", room, day);
        if (room == null || room.trim().isEmpty() || day == null) {
            throw new IllegalArgumentException("Залата и датата не могат да бъдат null или празни.");
        }

        List<Hour> hours = hourRepository.findByRoomAndDay(room, day);
        if (hours.isEmpty()) {
            throw new ResourceNotFoundException("Hour", "room and day", room + " и " + day);
        }

        return hours;
    }

    @Override
    public List<Hour> findByTypeAndDay(HourType type, LocalDate day) {
        logger.info("Извличане на часове от тип {} за дата {}", type, day);
        if (type == null || day == null) {
            throw new IllegalArgumentException("Типът и датата не могат да бъдат null.");
        }

        List<Hour> hours = hourRepository.findByTypeAndDay(type, day);
        if (hours.isEmpty()) {
            throw new ResourceNotFoundException("Hour", "type and day", type + " и " + day);
        }

        return hours;
    }

    @Override
    public List<Hour> findByGroupIgnoreCaseOrderByDayAscStartTimeAsc(String group) {
        logger.info("Извличане на всички часове за група {} (сортирани по ден и час)", group);
        if (group == null || group.trim().isEmpty()) {
            throw new IllegalArgumentException("Името на групата не може да бъде null или празно.");
        }

        List<Hour> hours = hourRepository.findByGroupIgnoreCaseOrderByDayAscStartTimeAsc(group);
        if (hours.isEmpty()) {
            throw new ResourceNotFoundException("Hour", "group", group);
        }

        return hours;
    }

    @Override
    public List<Hour> findByGroupIgnoreCaseAndDay(String group, LocalDate day) {
        logger.info("Извличане на часове за група {} в деня {}", group, day);
        if (group == null || group.trim().isEmpty() || day == null) {
            throw new IllegalArgumentException("Групата и датата не могат да бъдат null или празни.");
        }

        List<Hour> hours = hourRepository.findByGroupIgnoreCaseAndDay(group, day);
        if (hours.isEmpty()) {
            throw new ResourceNotFoundException("Hour", "group and day", group + " и " + day);
        }

        return hours;
    }

    @Override
    public List<Hour> findHourByRoom(String room) {
        logger.info("Извличане на часове в зала: {}", room);
        if (room == null || room.trim().isEmpty()) {
            throw new IllegalArgumentException("Залата не може да бъде null или празна.");
        }

        List<Hour> hours = hourRepository.findHourByRoom(room);
        if (hours.isEmpty()) {
            throw new ResourceNotFoundException("Hour", "room", room);
        }

        return hours;
    }

    @Override
    public List<Hour> findHourByDay(LocalDate day) {
        logger.info("Извличане на часове за дата: {}", day);
        if (day == null) {
            throw new IllegalArgumentException("Датата не може да бъде null.");
        }

        List<Hour> hours = hourRepository.findHourByDay(day);
        if (hours.isEmpty()) {
            throw new ResourceNotFoundException("Hour", "day", day.toString());
        }

        return hours;
    }

    @Override
    public List<Hour> findHourByType(HourType hourType) {
        logger.info("Извличане на часове от тип: {}", hourType);
        if (hourType == null) {
            throw new IllegalArgumentException("Типът на часа не може да бъде null.");
        }

        List<Hour> hours = hourRepository.findHourByType(hourType);
        if (hours.isEmpty()) {
            throw new ResourceNotFoundException("Hour", "type", hourType.toString());
        }

        return hours;
    }














}


