package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.practicum.shareit.Marker.Create;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {
    private long id;
    @NotBlank(groups = Create.class)
    private String name;
    @Email
    @NotBlank(groups = Create.class)
    private String email;
}
