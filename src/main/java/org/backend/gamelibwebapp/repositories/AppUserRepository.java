package org.backend.gamelibwebapp.repositories;

import org.backend.gamelibwebapp.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser,Long> {


    AppUser findByUsername(String username);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

}
