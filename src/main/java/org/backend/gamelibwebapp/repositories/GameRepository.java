package org.backend.gamelibwebapp.repositories;

import org.backend.gamelibwebapp.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game,Long> {

    boolean existsByTitle(String title);

    @Query(value = "select * from game where is_accepted = true",nativeQuery = true)
    List<Game> getAccepted();

    @Query(value = "select * from game where is_accepted = false",nativeQuery = true)
    List<Game> getNotAccepted();




}
