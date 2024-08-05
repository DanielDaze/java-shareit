package ru.practicum.shareit.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Entity
@Table(name = "REQUESTS")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "description")
    @NotBlank
    private String description;

    @ManyToOne
    @JoinColumn(name = "requestor_id")
    @ToString.Exclude
    private User requestor;

    @Column(name = "creation_date")
    private LocalDateTime created;
}