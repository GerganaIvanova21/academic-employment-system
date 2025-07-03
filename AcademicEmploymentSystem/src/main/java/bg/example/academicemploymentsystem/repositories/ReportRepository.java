package bg.example.academicemploymentsystem.repositories;

import bg.example.academicemploymentsystem.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    // Намиране на всички доклади, създадени от даден потребител
    List<Report> findByPreparedById(Long userId);

    // Намиране на всички доклади, насочени към даден потребител
    List<Report> findByPreparedToId(Long userId);

    // Намиране на доклади по дати
    List<Report> findByCreatedAtBetween(Date startDate, Date endDate);

    // Намиране на доклади по заглавие (case insensitive)
    List<Report> findByTitleContainingIgnoreCase(String title);

    // Намиране на доклади по подател и получател
    List<Report> findByPreparedByIdAndPreparedToId(Long preparedById, Long preparedToId);

    //Сортиране на докладите по дати
    List<Report> findAllByOrderByCreatedAtDesc();
    List<Report> findAllByOrderByCreatedAtAsc();

    @Query("SELECT r FROM Report r WHERE FUNCTION('YEAR', r.createdAt) = :year")
    List<Report> findByYear(@Param("year") int year);


}
