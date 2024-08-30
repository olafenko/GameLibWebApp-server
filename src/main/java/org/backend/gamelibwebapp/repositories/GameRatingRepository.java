package org.backend.gamelibwebapp.repositories;

import org.backend.gamelibwebapp.entities.GameRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRatingRepository extends JpaRepository<GameRating,Long> {

    @Query(value = "select * from game_ratings where user_id=:user_id and game_id=:game_id", nativeQuery = true)
    Optional<GameRating> getUserRating(@Param("user_id") Long user_id,@Param("game_id") Long game_id);

}
