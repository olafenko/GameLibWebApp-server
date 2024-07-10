package org.backend.gamelibwebapp.repositories;

import lombok.NonNull;
import org.backend.gamelibwebapp.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game,Long> {

    boolean existsByTitle(String title);

    @Query(value = "select * from game where is_accepted = true",nativeQuery = true)
    List<Game> getAccepted();

    @Query(value = "select * from game where is_accepted = false",nativeQuery = true)
    List<Game> getNotAccepted();


    Optional<Game> findById(@NonNull Long id);

}
