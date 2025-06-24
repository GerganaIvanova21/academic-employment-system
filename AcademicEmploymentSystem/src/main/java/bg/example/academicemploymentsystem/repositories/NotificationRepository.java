package bg.example.academicemploymentsystem.repositories;

import bg.example.academicemploymentsystem.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    //Optional<Notification> findNotificationById(Long id);

    List<Notification> findNotificationBySeen(Boolean seen);

    //Всички нотификации за конкретен потребител
    List<Notification> findByUserId(Long userId);


    //Непрочетени нотификации за потребител
    List<Notification> findByUserIdAndSeenFalse(Long userId);

    //Подредени по дата на създаване (новите най-отгоре)
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    //Всички стари и прочетени (например за администраторска чистка)
    List<Notification> findBySeenTrueAndCreatedAtBefore(LocalDateTime dateTime);

    //Брой непрочетени нотификации за потребител
    long countByUserIdAndSeenFalse(Long userId);




}
