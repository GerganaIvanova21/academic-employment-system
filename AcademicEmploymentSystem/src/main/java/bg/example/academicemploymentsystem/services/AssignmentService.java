package bg.example.academicemploymentsystem.services;

import bg.example.academicemploymentsystem.entities.Assignment;
import bg.example.academicemploymentsystem.entities.User;
import bg.example.academicemploymentsystem.type.AssignmentType;
import bg.example.academicemploymentsystem.type.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface AssignmentService {

    List<Assignment> findAll();

    List<Assignment> findAssignmentByTitle(String title);

    List<Assignment> findAssignmentByAssignedBy(User assignedBy);

    List<Assignment> findAssignmentByAssignedTo(User assignedTo);


    List<Assignment> findAssignmentByStatus(Status status);

    List<Assignment> findAssignmentByAssignmentType(AssignmentType assignmentType);

    //Всички задачи, възложени на потребител по статус - за да се изведат всички активни, завършени или
    // в изчакване задачи за конкретен потребител
    List<Assignment> findAssignmentByAssignedToAndStatus(User assignedTo, Status status);

    //Всички задачи, възложени от потребител, подредени по дата
    List<Assignment> findAssignmentByAssignedByOrderByDueDateDesc(User assignedBy);

    //Задачи със срок след определена дата (напр. предстоящи)
    List<Assignment> findAssignmentByAssignedToAndDueDateAfter(User assignedTo, LocalDateTime dueDate);

    //ПР: търсене на всички задачи с „доклад“ в заглавието
    List<Assignment> findByTitleContainingIgnoreCase(String title);

    // Намиране на задачи с определен статус и тип за конкретен потребител (възложен)
    List<Assignment> findByAssignedToAndStatusAndAssignmentType(User assignedTo, Status status, AssignmentType assignmentType);

    // Намиране на задачи с определен статус и тип за конкретен потребител (възложител)
    List<Assignment> findByAssignedByAndStatusAndAssignmentType(User assignedBy, Status status, AssignmentType assignmentType);


}
