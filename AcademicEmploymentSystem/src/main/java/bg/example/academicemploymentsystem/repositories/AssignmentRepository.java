package bg.example.academicemploymentsystem.repositories;

import bg.example.academicemploymentsystem.entities.Assignment;
import bg.example.academicemploymentsystem.entities.User;
import bg.example.academicemploymentsystem.type.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {

    Optional<Assignment> findAssignmentById(Integer id);

    List<Assignment> findAssignmentByTitle(String title);

    List<Assignment> findAssignmentByAssignedBy(User assignedBy);

    List<Assignment> findAssignmentByAssignedTo(User assignedTo);

    List<Assignment> findAssignmentByDate(Date date);

    List<Assignment> findAssignmentByStatus(Status status);
}
