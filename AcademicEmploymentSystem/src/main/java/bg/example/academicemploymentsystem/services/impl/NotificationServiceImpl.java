package bg.example.academicemploymentsystem.services.impl;


import bg.example.academicemploymentsystem.entities.Notification;
import bg.example.academicemploymentsystem.entities.User;
import bg.example.academicemploymentsystem.exceptions.ResourceNotFoundException;
import bg.example.academicemploymentsystem.repositories.NotificationRepository;
import bg.example.academicemploymentsystem.repositories.UserRepository;
import bg.example.academicemploymentsystem.services.NotificationService;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Notification> findAll() {
        logger.info("Извличане на всички нотификации.");
        List<Notification> notifications = notificationRepository.findAll();
        if (notifications.isEmpty()) {
            logger.warn("Няма налични нотификации в базата данни.");
        }
        return notifications;
    }

    @Override
    public List<Notification> findNotificationBySeen(Boolean seen) {
        logger.info("Извличане на нотификации със статус 'seen': {}", seen);
        List<Notification> notifications = notificationRepository.findNotificationBySeen(seen);

        if (notifications.isEmpty()) {
            logger.warn("Няма намерени нотификации със статус 'seen': {}", seen);
            throw new ResourceNotFoundException("Notification", "seen", seen);
        }

        return notifications;
    }

    @Override
    public List<Notification> findByUserId(Long userId) {
        logger.info("Извличане на нотификации за потребител с ID: {}", userId);
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        if (notifications.isEmpty()) {
            logger.warn("Няма намерени нотификации за потребител с ID: {}", userId);
            throw new ResourceNotFoundException("Notification", "userId", userId);
        }
        return notifications;
    }

    @Override
    public List<Notification> findByUserIdAndSeenFalse(Long userId) {
        logger.info("Извличане на непрочетени нотификации за потребител с ID: {}", userId);

        // Проверка дали потребителят съществува
        if (!userRepository.existsById(userId)) {
            logger.warn("Потребител с ID {} не е намерен", userId);
            throw new ResourceNotFoundException("User", "id", userId);
        }

        List<Notification> notifications = notificationRepository.findByUserIdAndSeenFalse(userId);

        if (notifications.isEmpty()) {
            logger.info("Няма непрочетени нотификации за потребител с ID: {}", userId);
        }

        return notifications;
    }

    @Override
    public List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId) {
        logger.info("Извличане на всички нотификации по потребител с ID: {}, сортирани по дата на създаване.", userId);

        if (!userRepository.existsById(userId)) {
            logger.warn("Потребител с ID {} не е намерен.", userId);
            throw new ResourceNotFoundException("User", "id", userId);
        }

        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);

        if (notifications.isEmpty()) {
            logger.info("Няма налични нотификации за потребител с ID: {}", userId);
        }

        return notifications;
    }

    public Notification createNotification(Long userId, String message) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setSeen(false);
        notification.setCreatedAt(LocalDateTime.now());

        Notification saved = notificationRepository.save(notification);
        logger.info("Създадено известие за потребител с ID {}: {}", userId, message);
        return saved;
    }

    @Transactional
    public void markAsSeen(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", notificationId));

        if (!notification.getSeen()) {
            notification.setSeen(true);
            notificationRepository.save(notification);
            logger.info("Известието с ID {} е маркирано като прочетено.", notificationId);
        }
    }

    public void deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", notificationId));

        notificationRepository.delete(notification);
        logger.info("Известието с ID {} беше изтрито.", notificationId);
    }

}
