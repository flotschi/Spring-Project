package at.spengergasse.persistence;

import at.spengergasse.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // fetch user and lazily maps
    Optional<User> findById( Long id );

    // JPQL
    // fetch user and eagerly maps
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.maps WHERE u.id = (:id)")
    public Optional<User> findById_eagerFetch( @Param("id") Long id );
}
