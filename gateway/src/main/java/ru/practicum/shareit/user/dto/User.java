package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.practicum.shareit.Marker;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {
    private long id;

    @NotBlank(groups = Marker.Create.class)
    private String name;

    @Email
    @NotBlank(groups = Marker.Create.class)
    private String email;
}
