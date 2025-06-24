package bg.example.academicemploymentsystem.services;

import bg.example.academicemploymentsystem.entities.Notification;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationService {

    List<Notification> findAll();

    List<Notification> findNotificationBySeen(Boolean seen);

    //Всички нотификации за конкретен потребител
    List<Notification> findByUserId(Long userId);


    //Непрочетени нотификации за потребител
    List<Notification> findByUserIdAndSeenFalse(Long userId);

    //Подредени по дата на създаване (новите най-отгоре)
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    //Всички стари и прочетени (например за администраторска чистка)
    List<Notification> findBySeenTrueAndCreatedAtBefore(LocalDateTime dateTime);
}
