package bg.example.academicemploymentsystem.services;

import bg.example.academicemploymentsystem.entities.Report;

import java.util.Date;
import java.util.List;

public interface ReportService {
    List<Report> findAll();

    Report saveReport(Report report);

    Report updateReport(Long reportId, Report updatedReportData);

    void deleteReport(Long id);

    List<Report> getByPreparedById(Long userId);

    List<Report> getByPreparedToId(Long userId);

    List<Report> getByCreatedAtBetween(Date startDate, Date endDate);

    List<Report> getByTitleContaining(String title);

    List<Report> getByPreparedByAndPreparedTo(Long preparedById, Long preparedToId);

    List<Report> getAllSortedByCreatedAtDesc();

    List<Report> getAllSortedByCreatedAtAsc();

    List<Report> getByYear(int year);
}
