package bg.example.academicemploymentsystem.services.impl;

import bg.example.academicemploymentsystem.dto.request.UpdateUserRequestDTO;
import bg.example.academicemploymentsystem.entities.User;
import bg.example.academicemploymentsystem.exceptions.ResourceNotFoundException;
import bg.example.academicemploymentsystem.repositories.UserRepository;
import bg.example.academicemploymentsystem.services.UserService;
import bg.example.academicemploymentsystem.type.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUserById(Long id) {
        logger.info("Извличане на потребител по ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Потребител с ID {} не е намерен.", id);
                    return new ResourceNotFoundException("User", "id", id);
                });
    }

    @Override
    public User getUserByEmail(String email) {
        logger.info("Извличане на потребител по имейл: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("Потребител с имейл {} не е намерен.", email);
                    return new ResourceNotFoundException("User", "email", email);
                });
    }

    @Override
    public User getUserByEgn(String egn) {
        logger.info("Търсене на потребител по ЕГН: {}", egn);
        return userRepository.findByEgn(egn)
                .orElseThrow(() -> {
                    logger.error("Потребител с ЕГН {} не е намерен.", egn);
                    return new ResourceNotFoundException("User", "EGN", egn);
                });
    }

    @Override
    public List<User> getAllUsers(){
        logger.info("Извличане на всички потребители от базата данни.");
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            logger.warn("Няма налични потребители в системата.");
        } else {
            logger.info("Намерени са {} потребители.", users.size());
        }

        return users;
    }

    @Override
    public boolean existsByEmail(String email) {
        logger.info("Проверка за съществуване на потребител с имейл: {}", email);
        boolean exists = userRepository.existsByEmail(email);

        if (exists) {
            logger.info("Потребител с имейл {} съществува.", email);
        } else {
            logger.warn("Не е намерен потребител с имейл {}.", email);
        }

        return exists;
    }

    @Override
    public boolean existsByEgn(String egn) {
        logger.info("Проверка за съществуване на потребител по ЕГН: {}", egn);
        boolean exists = userRepository.existsByEgn(egn);

        if (exists) {
            logger.info("Потребител с ЕГН {} съществува.", egn);
        } else {
            logger.warn("Не е намерен потребител с ЕГН {}.", egn);
        }

        return exists;
    }

    @Override
    public List<User> getUserByRole(Role role) {
        logger.info("Извличане на всички потребители с роля: {}", role);
        List<User> users = userRepository.findUserByRole(role);

        if (users.isEmpty()) {
            logger.warn("Няма намерени потребители с роля: {}", role);
        } else {
            logger.info("Намерени са {} потребители с роля: {}", users.size(), role);
        }

        return users;
    }
/*
    @Override
    public List<User> getUserByRoleAndDepartment(String departmentName, Role role) {
        logger.info("Търсене на потребители с роля {} в катедра: {}", role, departmentName);

        if (departmentName == null || departmentName.trim().isEmpty()) {
            logger.error("Името на катедрата не може да бъде празно.");
            throw new IllegalArgumentException("Името на катедрата не може да бъде празно.");
        }

        if (role == null) {
            logger.error("Ролята не може да бъде null.");
            throw new IllegalArgumentException("Ролята не може да бъде null.");
        }

        Department department = departmentRepository.findDepartmentByDepartmentName(departmentName)
                .orElseThrow(() -> {
                    logger.error("Катедра с име '{}' не е намерена.", departmentName);
                    return new ResourceNotFoundException("Department", "name", departmentName);
                });

        // Тук при нужда може да направим още проверки върху department

        return userRepository.findByRoleAndDepartmentDepartmentName(role, department.getDepartmentName());
    }

 */

    @Override
    public List<User> getUserByRoleAndDepartment(String departmentName, Role role) {
        logger.info("Извличане на потребители с роля '{}' от катедра '{}'", role, departmentName);

        if (departmentName == null || departmentName.trim().isEmpty()) {
            logger.error("Името на катедрата е празно или null");
            throw new IllegalArgumentException("Името на катедрата не може да бъде празно.");
        }

        if (role == null) {
            logger.error("Ролята е null");
            throw new IllegalArgumentException("Ролята не може да бъде null.");
        }

        List<User> users = userRepository.findByRoleAndDepartmentDepartmentName(role, departmentName);

        if (users.isEmpty()) {
            logger.warn("Не са намерени потребители с роля '{}' в катедра '{}'", role, departmentName);
        } else {
            logger.info("Намерени са {} потребители с роля '{}' в катедра '{}'", users.size(), role, departmentName);
        }

        return users;
    }

    @Override
    public List<User> getUserByRoleAndFacultyId(Role role, Long facultyId) {
        logger.info("Извличане на потребители с роля '{}' от факултет с ID {}", role, facultyId);

        if (role == null) {
            logger.error("Ролята е null");
            throw new IllegalArgumentException("Ролята не може да бъде null.");
        }

        if (facultyId == null || facultyId <= 0) {
            logger.error("Некоректен facultyId: {}", facultyId);
            throw new IllegalArgumentException("ID на факултет трябва да бъде положително число.");
        }

        List<User> users = userRepository.findByRoleAndDepartmentFacultyId(role, facultyId);

        if (users.isEmpty()) {
            logger.warn("Не са намерени потребители с роля '{}' във факултет с ID {}", role, facultyId);
        } else {
            logger.info("Намерени са {} потребители с роля '{}' във факултет с ID {}", users.size(), role, facultyId);
        }

        return users;
    }


    @Override
    public List<User> getUserByRoleAndFacultyName(Role role, String facultyName) {
        logger.info("Търсене на потребители с роля {} във факултет '{}'", role, facultyName);

        if (facultyName == null || facultyName.trim().isEmpty()) {
            logger.warn("Името на факултета е празно.");
            throw new IllegalArgumentException("Името на факултета не може да бъде празно");
        }

        List<User> users = userRepository.findByRoleAndDepartmentFacultyFacultyName(role, facultyName);

        if (users.isEmpty()) {
            logger.warn("Няма намерени потребители с роля {} във факултет '{}'", role, facultyName);
            throw new ResourceNotFoundException("Users", "role and faculty name", role + ", " + facultyName);
        }

        return users;
    }

    @Override
    public List<User> getUserByRoleAndFacultyNameContaining(Role role, String facultyNamePart) {
        logger.info("Частично търсене на потребители с роля {} по част от името на факултета: '{}'", role, facultyNamePart);

        if (facultyNamePart == null || facultyNamePart.trim().isEmpty()) {
            logger.warn("Част от името на факултета е празна.");
            throw new IllegalArgumentException("Частта от името на факултета не може да бъде празна");
        }

        List<User> users = userRepository.findByRoleAndFacultyNameContainingIgnoreCase(role, facultyNamePart);

        if (users.isEmpty()) {
            logger.warn("Не са намерени потребители с роля {} във факултет, съдържащ '{}'", role, facultyNamePart);
            throw new ResourceNotFoundException("Users", "role and partial faculty name", role + ", " + facultyNamePart);
        }

        return users;
    }

    @Override
    public List<User> searchUserByName(String name) {
        logger.info("Търсене на потребители по име: {}", name);

        if (name == null || name.trim().isEmpty()) {
            logger.warn("Подадено е празно или null име за търсене.");
            throw new IllegalArgumentException("Името за търсене не може да бъде празно.");
        }

        List<User> users = userRepository.searchByName(name.trim());

        if (users.isEmpty()) {
            logger.info("Не са намерени потребители с име съдържащо: {}", name);
        } else {
            logger.info("Намерени са {} потребителя(и) с име съдържащо: {}", users.size(), name);
        }

        return users;
    }
    @Override
    public List<User> searchUserByEmailPart(String email) {
        logger.info("Търсене на потребител по имейл: {}", email);

        if (email == null || email.trim().isEmpty()) {
            logger.warn("Подаденият имейл е празен или null.");
            throw new IllegalArgumentException("Имейл адреса не може да бъде null или празен.");
        }

        return userRepository.findByEmailContainingIgnoreCase(email.trim());
    }

    @Override
    public User saveUser(User user) {
        logger.info("Създаване на нов потребител: {}", user.getEmail());

        // 1. Проверка дали потребителят с този email вече съществува
        if (userRepository.existsByEmail(user.getEmail())) {
            logger.warn("Потребител с имейл {} вече съществува.", user.getEmail());
            throw new IllegalArgumentException("Потребител с този имейл вече съществува.");
        }

        // 2. Проверка дали потребител с това ЕГН вече съществува
        if (userRepository.existsByEgn(user.getEgn())) {
            logger.warn("Потребител с ЕГН {} вече съществува.", user.getEgn());
            throw new IllegalArgumentException("Потребител с това ЕГН вече съществува.");
        }

        // 3. Проверка дали текущият потребител е администратор
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.error("Неавторизиран опит за създаване на потребител.");
            throw new SecurityException("Неоторизиран достъп.");
        }

        User currentUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", authentication.getName()));

        if (!currentUser.getRole().equals(Role.ADMIN)) {
            logger.warn("Потребител {} няма права да създава нови потребители.", currentUser.getEmail());
            throw new SecurityException("Само администратори могат да създават нови потребители.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        logger.info("Успешно създаден потребител с ID: {}", savedUser.getId());

        return savedUser;
    }

    @Override
    public User updateUser(Long id, UpdateUserRequestDTO userUpdateDTO) {
        logger.info("Обновяване данните на потребител с ID: {}", id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.error("Неавторизиран опит за редакция.");
            throw new SecurityException("Неоторизиран достъп.");
        }

        User currentUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", authentication.getName()));

        if (!currentUser.getRole().name().equals("ADMIN") && !currentUser.getId().equals(id)) {
            logger.warn("Потребител с ID {} няма права да редактира потребител с ID {}", currentUser.getId(), id);
            throw new SecurityException("Недостатъчни права за редакция.");
        }

        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        // Проверка дали новият имейл вече се използва от друг потребител
        if (userUpdateDTO.getEmail() != null && !userUpdateDTO.getEmail().equals(userToUpdate.getEmail())) {
            boolean emailExists = userRepository.existsByEmail(userUpdateDTO.getEmail());
            if (emailExists) {
                logger.warn("Имейлът '{}' вече е зает от друг потребител.", userUpdateDTO.getEmail());
                throw new IllegalArgumentException("Имейлът вече се използва от друг потребител.");
            }
            userToUpdate.setEmail(userUpdateDTO.getEmail());
        }

        if (userUpdateDTO.getFirstName() != null) userToUpdate.setFirstName(userUpdateDTO.getFirstName());
        if (userUpdateDTO.getMiddleName() != null) userToUpdate.setMiddleName(userUpdateDTO.getMiddleName());
        if (userUpdateDTO.getLastName() != null) userToUpdate.setLastName(userUpdateDTO.getLastName());
        if (userUpdateDTO.getPassword() != null) userToUpdate.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));

        logger.info("Успешно обновен потребител с ID {}", id);
        return userRepository.save(userToUpdate);
    }

    @Override
    public void deleteUserById(Long userId) {
        logger.info("Изтриване на потребител с ID: {}", userId);

        // Само администратор има право
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            logger.warn("Неавторизиран достъп до изтриване.");
            throw new SecurityException("Неоторизиран достъп.");
        }

        User currentUser = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", auth.getName()));

        if (currentUser.getRole() != Role.ADMIN) {
            logger.warn("Потребител с роля {} няма право да изтрива други потребители.", currentUser.getRole());
            throw new SecurityException("Само администратор може да изтрива потребители.");
        }

        User userToDelete = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "ID", userId));

        userToDelete.setDeletedAt(LocalDateTime.now());
        userRepository.save(userToDelete);

        logger.info("Потребител с ID {} е отбелязан като изтрит.", userId);
    }






























    /*public List<User> findAll() {
        logger.info("Извличане на всички потребители от базата данни.");
        List<User> users = userRepository.findAll();
        logger.debug("Намерени са {} потребителя.", users.size());
        return users;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        logger.info("Търсене на потребител по имейл: {}", email);
        Optional<User> result = userRepository.findByEmail(email);
        if (result.isPresent()) {
            logger.debug("Потребителят с имейл {} е намерен: {}", email, result.get());
        } else {
            logger.warn("Потребителят с имейл {} не е открит.", email);
        }
        return result;
    }

    @Override
    public List<User> findUserByFirstNameAndLastName(String firstName, String lastName) {
        logger.info("Търсене на потребител по име и фамилия: {} {}", firstName, lastName);
        List<User> users = userRepository.findUserByFirstNameAndLastName(firstName, lastName);

        if (users.isEmpty()) {
            logger.warn("Не е намерен потребител с такова име и фамилия: {} {}", firstName, lastName);
            throw new NoSuchElementException("Не е намерен потребител с име: " + firstName + " " + lastName);
        }

        logger.debug("Намерени потребители: {}", users.size());
        users.forEach(user -> logger.info("Намерен потребител: {} {} (ID: {})",
                user.getFirstName(), user.getLastName(), user.getId()));

        return users;
    }


    @Override
    public List<User> findUserByFirstNameAndMiddleNameAndLastName(String firstName, String middleName, String lastName) {
        logger.info("Търсене на потребител по име, презиме и фамилия: {} {} {}", firstName, middleName, lastName);
        List<User> users = userRepository.findUserByFirstNameAndMiddleNameAndLastName(firstName, middleName, lastName);

        if (users.isEmpty()) {
            logger.warn("Не е намерен потребител с име: {} {} {}", firstName, middleName, lastName);
            throw new NoSuchElementException("Не е намерен потребител с име: " +
                    firstName + " " + middleName + " " + lastName);
        }

        logger.debug("Намерени потребители: {}", users.size());
        users.forEach(user -> logger.info("Намерен потребител: {} {} {} (ID: {})",
                user.getFirstName(), user.getMiddleName(), user.getLastName(), user.getId()));

        return users;
    }


    @Override
    public List<User> findUserByRole(Role role) {
        logger.info("Търсене на потребители с определена роля: {}", role);

        List<User> users = userRepository.findUserByRole(role);

        if (users.isEmpty()) {
            String message = String.format("Не са намерени потребители с роля: %s", role);
            logger.warn(message);
            throw new NoSuchElementException(message);
        }

        logger.debug("Намерени потребители с роля {}: {}", role, users.size());
        return users;
    }


    @Override
    public List<User> findByDepartmentDepartmentName(String departmentName) {
        logger.info("Търсене на потребители по име на катедра: {}", departmentName);
        List<User> users = userRepository.findByDepartmentDepartmentName(departmentName);
        if (users.isEmpty()) {
            logger.warn("Няма намерени потребители в катедра: {}", departmentName);
        } else {
            logger.debug("Намерени са {} потребители в катедра: {}", users.size(), departmentName);
        }
        return users;
    }

    @Override
    public List<User> findByRoleAndDepartmentDepartmentName(Role role, String departmentName) {
        logger.info("Търсене на потребители с роля '{}' в катедра '{}'", role, departmentName);

        List<User> users = userRepository.findByRoleAndDepartmentDepartmentName(role, departmentName);

        if (users.isEmpty()) {
            logger.warn("Не са открити потребители с роля '{}' в катедра '{}'", role, departmentName);
        } else {
            logger.debug("Намерени са {} потребителя с роля '{}' в катедра '{}'", users.size(), role, departmentName);
        }

        return users;
    }*/



}
