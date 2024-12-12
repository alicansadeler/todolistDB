package com.alicansadeler.todolist.entity;

import com.alicansadeler.todolist.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(schema = "public", name = "todo_users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Username cannot be null and blank")
    @Size(max = 50, message = "Username cannot be more than 50 characters")
    private String username;

    @NotBlank(message = "Username cannot be null and blank")
    @Size(max = 255, message = "Password cannot be more than 255 characters")
    private String password;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Timestamp.valueOf(LocalDateTime.now());
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Tasks> tasksSet = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;
    private boolean enabled = true;
    private boolean accountNonLocked = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
