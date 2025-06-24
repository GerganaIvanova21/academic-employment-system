package bg.example.academicemploymentsystem.repositories;

import bg.example.academicemploymentsystem.entities.Hour;
import bg.example.academicemploymentsystem.type.HourType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface HourRepository extends JpaRepository<Hour, Long> {
    //Optional<Hour> findHourById(Long id);

    List<Hour> findHourByCourseId(Long courseId);

    List<Hour> findHourByUserId(Long userId);

    List<Hour> findHourByDay(LocalDate day);

    List<Hour> findHourByType(HourType hourType);

    List<Hour> findHourByRoom(String room);

    //Часове на потребител за конкретна дата - за да покаже графика за деня
    List<Hour> findByUserIdAndDay(Long userId, LocalDate day);

    //Всички часове за потребител, подредени по дата и час
    List<Hour> findByUserIdOrderByDayAscStartTimeAsc(Long userId);

    //Часове по стая и ден
    List<Hour> findByRoomAndDay(String room, LocalDate day);

    //Тъесене по тип и дата
    List<Hour> findByTypeAndDay(HourType type, LocalDate day);

    //Филтриране по тип, курс и преподавател
    List<Hour> findByTypeAndCourseIdAndUserId(HourType type, Long courseId, Long userId);

    //Часове за курс по конкретна дата - показва занятия по дисциплина за деня
    List<Hour> findByCourseIdAndDay(Long courseId, LocalDate day);

    // Часове за курс, сортирани по дата и начален час - проследява последователността на провеждане на курса
    List<Hour> findByCourseIdOrderByDayAscStartTimeAsc(Long courseId);

    //Проверка дали стаята е заета в определен времеви интервал
    boolean existsByRoomAndDayAndStartTimeLessThanAndEndTimeGreaterThan(
            String room, LocalDate day, LocalTime end, LocalTime start);

    //Всички часове за определен тип (лекции, упражнения) в дадена седмица
    List<Hour> findByTypeAndDayBetween(HourType type, LocalDate start, LocalDate end);




}
