package bg.example.academicemploymentsystem.services.impl;


import bg.example.academicemploymentsystem.entities.Assignment;
import bg.example.academicemploymentsystem.entities.User;
import bg.example.academicemploymentsystem.exceptions.ResourceNotFoundException;
import bg.example.academicemploymentsystem.repositories.AssignmentRepository;
import bg.example.academicemploymentsystem.services.AssignmentService;
import bg.example.academicemploymentsystem.type.AssignmentType;
import bg.example.academicemploymentsystem.type.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    @Autowired
    public AssignmentServiceImpl(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    @Override
    public List<Assignment> findAll() {
        List<Assignment> assignments = assignmentRepository.findAll();
        if (assignments.isEmpty()) {
            logger.warn("Няма налични задачи в системата");
            throw new ResourceNotFoundException("Assignment", "all", "empty");
        }
        logger.info("Извлечени са {} задачи", assignments.size());
        return assignments;
    }

    @Override
    public List<Assignment> findAssignmentByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Заглавието не може да бъде празно");
        }
        List<Assignment> assignments = assignmentRepository.findAssignmentByTitle(title);
        if (assignments.isEmpty()) {
            throw new ResourceNotFoundException("Assignment", "title", title);
        }
        logger.info("Намерени са {} задачи със заглавие: {}", assignments.size(), title);
        return assignments;
    }

    @Override
    public List<Assignment> findAssignmentByAssignedBy(User assignedBy) {
        validateUser(assignedBy, "assignedBy");
        List<Assignment> assignments = assignmentRepository.findAssignmentByAssignedBy(assignedBy);
        if (assignments.isEmpty()) {
            throw new ResourceNotFoundException("Assignment", "assignedBy", assignedBy.getId());
        }
        logger.info("Намерени са {} задачи, възложени от потребител с ID {}", assignments.size(), assignedBy.getId());
        return assignments;
    }

    @Override
    public List<Assignment> findAssignmentByAssignedTo(User assignedTo) {
        validateUser(assignedTo, "assignedTo");
        List<Assignment> assignments = assignmentRepository.findAssignmentByAssignedTo(assignedTo);
        if (assignments.isEmpty()) {
            throw new ResourceNotFoundException("Assignment", "assignedTo", assignedTo.getId());
        }
        logger.info("Намерени са {} задачи за потребител с ID {}", assignments.size(), assignedTo.getId());
        return assignments;
    }

    @Override
    public List<Assignment> findAssignmentByStatus(Status status) {
        if (status == null) {
            throw new IllegalArgumentException("Статусът не може да бъде null");
        }
        List<Assignment> assignments = assignmentRepository.findAssignmentByStatus(status);
        if (assignments.isEmpty()) {
            throw new ResourceNotFoundException("Assignment", "status", status);
        }
        logger.info("Намерени са {} задачи със статус {}", assignments.size(), status);
        return assignments;
    }

    @Override
    public List<Assignment> findAssignmentByAssignmentType(AssignmentType assignmentType) {
        if (assignmentType == null) {
            throw new IllegalArgumentException("Типът на задачата не може да бъде null");
        }
        List<Assignment> assignments = assignmentRepository.findAssignmentByAssignmentType(assignmentType);
        if (assignments.isEmpty()) {
            throw new ResourceNotFoundException("Assignment", "assignmentType", assignmentType);
        }
        logger.info("Намерени са {} задачи с тип {}", assignments.size(), assignmentType);
        return assignments;
    }

    @Override
    public List<Assignment> findAssignmentByAssignedToAndStatus(User assignedTo, Status status) {
        validateUser(assignedTo, "assignedTo");
        if (status == null) {
            throw new IllegalArgumentException("Статусът не може да бъде null");
        }
        List<Assignment> assignments = assignmentRepository.findAssignmentByAssignedToAndStatus(assignedTo, status);
        if (assignments.isEmpty()) {
            throw new ResourceNotFoundException("Assignment", "assignedTo/status", assignedTo.getId() + "/" + status);
        }
        logger.info("Намерени са {} задачи за потребител ID {} със статус {}", assignments.size(), assignedTo.getId(), status);
        return assignments;
    }

    @Override
    public List<Assignment> findAssignmentByAssignedByOrderByDueDateDesc(User assignedBy) {
        validateUser(assignedBy, "assignedBy");
        List<Assignment> assignments = assignmentRepository.findAssignmentByAssignedByOrderByDueDateDesc(assignedBy);
        if (assignments.isEmpty()) {
            throw new ResourceNotFoundException("Assignment", "assignedBy (orderByDueDateDesc)", assignedBy.getId());
        }
        logger.info("Намерени са {} задачи, възложени от потребител с ID {} (сортирани по краен срок)", assignments.size(), assignedBy.getId());
        return assignments;
    }

    @Override
    public List<Assignment> findAssignmentByAssignedToAndDueDateAfter(User assignedTo, LocalDateTime dueDate) {
        validateUser(assignedTo, "assignedTo");
        if (dueDate == null) {
            throw new IllegalArgumentException("Крайният срок не може да бъде null");
        }
        List<Assignment> assignments = assignmentRepository.findAssignmentByAssignedToAndDueDateAfter(assignedTo, dueDate);
        if (assignments.isEmpty()) {
            throw new ResourceNotFoundException("Assignment", "assignedTo/afterDueDate", assignedTo.getId() + "/" + dueDate);
        }
        logger.info("Намерени са {} бъдещи задачи за потребител с ID {}", assignments.size(), assignedTo.getId());
        return assignments;
    }

    @Override
    public List<Assignment> findByTitleContainingIgnoreCase(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Търсеният текст не може да бъде празен");
        }
        List<Assignment> assignments = assignmentRepository.findByTitleContainingIgnoreCase(title);
        if (assignments.isEmpty()) {
            throw new ResourceNotFoundException("Assignment", "title (partial)", title);
        }
        logger.info("Намерени са {} задачи, съдържащи '{}' в заглавието", assignments.size(), title);
        return assignments;
    }

    @Override
    public List<Assignment> findByAssignedToAndStatusAndAssignmentType(User assignedTo, Status status, AssignmentType assignmentType) {
        validateUser(assignedTo, "assignedTo");
        if (status == null || assignmentType == null) {
            throw new IllegalArgumentException("Статусът и типът на задачата не могат да бъдат null");
        }
        List<Assignment> assignments = assignmentRepository.findByAssignedToAndStatusAndAssignmentType(assignedTo, status, assignmentType);
        if (assignments.isEmpty()) {
            throw new ResourceNotFoundException("Assignment", "assignedTo/status/type", assignedTo.getId() + "/" + status + "/" + assignmentType);
        }
        logger.info("Намерени са {} задачи за потребител с ID {}, статус {} и тип {}", assignments.size(), assignedTo.getId(), status, assignmentType);
        return assignments;
    }

    @Override
    public List<Assignment> findByAssignedByAndStatusAndAssignmentType(User assignedBy, Status status, AssignmentType assignmentType) {
        validateUser(assignedBy, "assignedBy");
        if (status == null || assignmentType == null) {
            throw new IllegalArgumentException("Статусът и типът на задачата не могат да бъдат null");
        }
        List<Assignment> assignments = assignmentRepository.findByAssignedByAndStatusAndAssignmentType(assignedBy, status, assignmentType);
        if (assignments.isEmpty()) {
            throw new ResourceNotFoundException("Assignment", "assignedBy/status/type", assignedBy.getId() + "/" + status + "/" + assignmentType);
        }
        logger.info("Намерени са {} задачи, възложени от потребител с ID {}, статус {} и тип {}", assignments.size(), assignedBy.getId(), status, assignmentType);
        return assignments;
    }

    private void validateUser(User user, String role) {
        if (user == null || user.getId() == null) {
            logger.warn("Потребителят ({}) е null или няма ID", role);
            throw new IllegalArgumentException("Потребителят (" + role + ") не може да бъде null и трябва да има ID");
        }
    }







}
