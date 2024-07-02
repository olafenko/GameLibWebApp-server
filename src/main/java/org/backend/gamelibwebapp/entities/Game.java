package org.backend.gamelibwebapp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table
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
    @OneToMany
    private List<GameRating> ratings;
    private String imageUrl;
    private boolean isAccepted;




}
