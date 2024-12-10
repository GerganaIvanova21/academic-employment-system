package bg.example.academicemploymentsystem.repositories;

import bg.example.academicemploymentsystem.entities.Hour;
import bg.example.academicemploymentsystem.type.HourType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface HourRepository extends JpaRepository<Hour, Integer> {
    Optional<Hour> findHourById(Integer id);

    List<Hour> findHourByCourseId(Integer courseId);

    List<Hour> findHourByUserId(Integer userId);

    List<Hour> findHourByDay(Date day);

    List<Hour> findHourByType(HourType hourType);

    List<Hour> findHourByRoom(String room);
}
