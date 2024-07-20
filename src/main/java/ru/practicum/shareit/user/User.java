package ru.practicum.shareit.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.practicum.shareit.Marker.Create;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(groups = Create.class)
    @Column(name = "name")
    private String name;

    @Email
    @NotBlank(groups = Create.class)
    @Column(name = "email")
    private String email;
}
