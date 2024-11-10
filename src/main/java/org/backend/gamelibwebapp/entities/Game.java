package org.backend.gamelibwebapp.entities;

import jakarta.persistence.*;
import lombok.*;
import org.backend.gamelibwebapp.entities.enums.GameCategory;

import java.util.List;

@Entity
@Table(name = "games")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String producer;
    @Enumerated(value = EnumType.STRING)
    private List<GameCategory> gameCategory;
    private String imageUrl;
    private String description;
    private boolean isAccepted;


}
