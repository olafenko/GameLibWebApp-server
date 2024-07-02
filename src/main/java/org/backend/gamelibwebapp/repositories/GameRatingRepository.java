package org.backend.gamelibwebapp.repositories;

import org.backend.gamelibwebapp.entities.GameRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRatingRepository extends JpaRepository<GameRating,Long> {

    @Query(value = "select count(id) from game_rating where game_id=:game_id",nativeQuery = true)
    Integer getRatingsOfGame(@Param("game_id") Long game_id);


}
