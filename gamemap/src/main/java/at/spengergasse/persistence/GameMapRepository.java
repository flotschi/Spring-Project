package at.spengergasse.persistence;

import at.spengergasse.domain.GameMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface GameMapRepository extends JpaRepository<GameMap, Long> {

    Optional<GameMap> findById( Long id );
}
