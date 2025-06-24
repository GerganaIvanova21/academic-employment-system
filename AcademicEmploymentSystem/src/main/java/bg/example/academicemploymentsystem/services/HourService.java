package bg.example.academicemploymentsystem.services;

import bg.example.academicemploymentsystem.entities.Hour;
import bg.example.academicemploymentsystem.type.HourType;

import java.time.LocalDate;
import java.util.List;

public interface HourService {
    List<Hour> findAll();

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
}
