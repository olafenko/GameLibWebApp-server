package org.backend.gamelibwebapp.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "game_ratings")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double rate;

    @ManyToOne
    private AppUser user;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Game game;


}
