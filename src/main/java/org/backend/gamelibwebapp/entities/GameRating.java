package org.backend.gamelibwebapp.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rate;

    @ManyToOne
    private AppUser user;

    @ManyToOne
    private Game game;


}
