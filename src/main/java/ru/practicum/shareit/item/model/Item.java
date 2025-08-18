package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.model.User;

@ToString
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items", schema = "public")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String name ;
    private String description ;
    private Boolean available ;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner ;
}
