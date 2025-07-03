package bg.example.academicemploymentsystem.entities;

import bg.example.academicemploymentsystem.type.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
@SQLDelete(sql = "UPDATE _user SET deleted_at = NOW() WHERE user_id = ?")
@Where(clause = "deleted_at IS NULL")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Basic
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "password")
    private String password;

    @Basic
    @Column(name = "first_name")
    private String firstName;

    @Basic
    @Column(name = "middle_name")
    private String middleName;

    @Basic
    @Column(name = "last_name")
    private String lastName;

    @Basic
    @Column(name = "egn")
    private String egn;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Basic
    @Column(name = "deleted")
    private boolean deleted = false;

    @Basic
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Basic
    @Column(name = "deleted_by")
    private String deletedBy;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @OneToMany(mappedBy = "assignedBy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Assignment> assignmentsCreated;

    @OneToMany(mappedBy = "assignedTo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Assignment> assignmentsReceived;

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Evaluation> evaluationsCreated;

    @OneToMany(mappedBy = "createdTo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Evaluation> evaluationsReceived;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Hour> hours = new HashSet<>();

    @OneToMany(mappedBy = "preparedBy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Report> reportsCreated;

    @OneToMany(mappedBy = "preparedTo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Report> reportsReceived;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Course> courses = new HashSet<>();



    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }




}
