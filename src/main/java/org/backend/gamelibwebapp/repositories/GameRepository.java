package org.backend.gamelibwebapp.repositories;

import org.backend.gamelibwebapp.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    boolean existsByTitle(String title);

}
