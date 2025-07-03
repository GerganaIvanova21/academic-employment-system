package bg.example.academicemploymentsystem.services.impl;

import bg.example.academicemploymentsystem.entities.Report;
import bg.example.academicemploymentsystem.entities.User;
import bg.example.academicemploymentsystem.exceptions.ResourceNotFoundException;
import bg.example.academicemploymentsystem.repositories.ReportRepository;
import bg.example.academicemploymentsystem.services.ReportService;
import bg.example.academicemploymentsystem.services.UserService;
import bg.example.academicemploymentsystem.type.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private final ReportRepository reportRepository;
    private final UserService userService;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository, UserService userService){
        this.reportRepository = reportRepository;
        this.userService = userService;
    }

    @Override
    public List<Report> findAll() {
        logger.info("Извличане на всички доклади.");
        List<Report> reports = reportRepository.findAll();
        if (reports.isEmpty()) {
            logger.warn("Няма налични доклади в базата данни.");
        }
        return reports;
    }

    @Override
    public List<Report> getByPreparedById(Long userId) {
        if (userId == null) {
            logger.warn("ID на подготвящия доклада потребител не може да бъде null.");
            throw new IllegalArgumentException("PreparedById не може да бъде null.");
        }
        logger.info("Извличане на доклади, подготвени от потребител с ID: {}", userId);
        return reportRepository.findByPreparedById(userId);
    }

    @Override
    public List<Report> getByPreparedToId(Long userId) {
        if (userId == null) {
            logger.warn("ID на получателя на доклада не може да бъде null.");
            throw new IllegalArgumentException("PreparedToId не може да бъде null.");
        }
        logger.info("Извличане на доклади, насочени към потребител с ID: {}", userId);
        return reportRepository.findByPreparedToId(userId);
    }

    @Override
    public List<Report> getByCreatedAtBetween(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            logger.warn("Начална или крайна дата е null.");
            throw new IllegalArgumentException("И двете дати (начална и крайна) трябва да бъдат зададени.");
        }
        if (startDate.after(endDate)) {
            logger.warn("Началната дата {} е след крайната дата {}.", startDate, endDate);
            throw new IllegalArgumentException("Началната дата не може да бъде след крайната дата.");
        }
        logger.info("Извличане на доклади между {} и {}", startDate, endDate);
        return reportRepository.findByCreatedAtBetween(startDate, endDate);
    }

    @Override
    public List<Report> getByTitleContaining(String title) {
        if (title == null || title.trim().isEmpty()) {
            logger.warn("Заглавието за търсене е празно.");
            throw new IllegalArgumentException("Заглавието не може да бъде празно.");
        }
        logger.info("Търсене на доклади, съдържащи заглавие: {}", title);
        return reportRepository.findByTitleContainingIgnoreCase(title.trim());
    }

    @Override
    public List<Report> getByPreparedByAndPreparedTo(Long preparedById, Long preparedToId) {
        if (preparedById == null || preparedToId == null) {
            logger.warn("ID-тата на подател или получател са null.");
            throw new IllegalArgumentException("И двата идентификатора трябва да бъдат зададени.");
        }
        logger.info("Извличане на доклади от потребител {} към потребител {}", preparedById, preparedToId);
        return reportRepository.findByPreparedByIdAndPreparedToId(preparedById, preparedToId);
    }

    @Override
    public List<Report> getAllSortedByCreatedAtDesc() {
        logger.info("Извличане на всички доклади, сортирани по дата на създаване (низходящо)");
        List<Report> reports = reportRepository.findAllByOrderByCreatedAtDesc();
        if (reports.isEmpty()) {
            logger.warn("Няма намерени доклади.");
        }
        return reports;
    }

    @Override
    public List<Report> getAllSortedByCreatedAtAsc() {
        logger.info("Извличане на всички доклади, сортирани по дата на създаване (възходящо)");
        List<Report> reports = reportRepository.findAllByOrderByCreatedAtAsc();
        if (reports.isEmpty()) {
            logger.warn("Няма намерени доклади.");
        }
        return reports;
    }

    @Override
    public List<Report> getByYear(int year) {
        int currentYear = LocalDate.now().getYear();
        if (year < 1900 || year > currentYear) {
            logger.warn("Въведена е невалидна година: {}", year);
            throw new IllegalArgumentException("Невалидна година за търсене.");
        }
        logger.info("Извличане на доклади за година: {}", year);
        return reportRepository.findByYear(year);
    }

    @Override
    public Report saveReport(Report report) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            logger.warn("Опит за създаване на доклад без валидна автентикация");
            throw new SecurityException("Потребителят трябва да е влязъл в системата.");
        }

        boolean isTeacher = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equalsIgnoreCase("TEACHER"));

        if (!isTeacher) {
            logger.warn("Потребител {} няма роля TEACHER и няма право да създава доклади", auth.getName());
            throw new IllegalArgumentException("Само преподаватели могат да създават доклади.");
        }

        User currentUser = userService.getUserByEmail(auth.getName());
        if (!currentUser.getId().equals(report.getPreparedBy().getId())) {
            logger.warn("Потребител {} се опитва да създаде доклад от името на друг потребител {}", currentUser.getId(), report.getPreparedBy().getId());
            throw new SecurityException("Не може да се създава доклад от името на друг потребител.");
        }

        // Проверка дали получателят е ръководител катедра
        Long preparedToId = report.getPreparedTo().getId();
        User preparedToUser = userService.getUserById(preparedToId);
        if (!(preparedToUser.getRole() == Role.DEPARTMENT_HEAD)) {
            logger.warn("Докладът трябва да бъде изпратен към ръководител катедра, но получателят с ID {} няма такава роля.", preparedToId);
            throw new IllegalArgumentException("Докладът трябва да бъде изпратен към ръководител катедра.");
        }

        report.setCreatedAt(new Date());

        Report savedReport = reportRepository.save(report);
        logger.info("Доклад с ID {} е създаден от потребител с ID {}", savedReport.getId(), currentUser.getId());
        return savedReport;
    }

    @Override
    public Report updateReport(Long reportId, Report updatedReportData) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            logger.warn("Опит за обновяване на доклад без валидна автентикация");
            throw new SecurityException("Потребителят трябва да е влязъл в системата.");
        }

        Report existingReport = reportRepository.findById(reportId)
                .orElseThrow(() -> {
                    logger.warn("Доклад с ID {} не е намерен за обновяване", reportId);
                    return new ResourceNotFoundException("Report", "id", reportId);
                });

        User currentUser = userService.getUserByEmail(auth.getName());

        // Проверка дали текущият потребител е автор на доклада
        if (!existingReport.getPreparedBy().getId().equals(currentUser.getId())) {
            logger.warn("Потребител {} се опитва да обнови доклад, който не е негов", currentUser.getId());
            throw new SecurityException("Нямате права да обновявате този доклад.");
        }

        // Ако има промяна на получателя, проверка ролята му
        if (!existingReport.getPreparedTo().getId().equals(updatedReportData.getPreparedTo().getId())) {
            User newPreparedTo = userService.getUserById(updatedReportData.getPreparedTo().getId());
            if (!(newPreparedTo.getRole() == Role.DEPARTMENT_HEAD)) {
                logger.warn("Новият получател с ID {} няма роля ръководител катедра", newPreparedTo.getId());
                throw new IllegalArgumentException("Докладът трябва да бъде изпратен към ръководител катедра.");
            }
            existingReport.setPreparedTo(newPreparedTo);
        }

        // Обновяване на полета заглавие и съдържание
        existingReport.setTitle(updatedReportData.getTitle());
        existingReport.setReportText(updatedReportData.getReportText());

        Report savedReport = reportRepository.save(existingReport);
        logger.info("Доклад с ID {} е обновен от потребител с ID {}", savedReport.getId(), currentUser.getId());
        return savedReport;
    }

    @Override
    public void deleteReport(Long reportId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            logger.warn("Изтриване на доклад без валидна автентикация");
            throw new SecurityException("Потребителят трябва да е влязъл в системата.");
        }

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> {
                    logger.warn("Доклад с ID {} не е намерен за изтриване", reportId);
                    return new ResourceNotFoundException("Report", "id", reportId);
                });

        User currentUser = userService.getUserByEmail(auth.getName());

        // Проверка дали текущият потребител е автор или има
        boolean isAuthor = report.getPreparedBy().getId().equals(currentUser.getId());


        if (!isAuthor) {
            logger.warn("Потребител с ID {} няма права да изтрие доклад с ID {}", currentUser.getId(), reportId);
            throw new SecurityException("Нямате права да изтривате този доклад.");
        }

        reportRepository.delete(report);
        logger.info("Доклад с ID {} е изтрит от потребител с ID {}", reportId, currentUser.getId());
    }






}
