package hr.domagoj.servicepilot.repos;

import hr.domagoj.servicepilot.entities.RefreshToken;
import hr.domagoj.servicepilot.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    void deleteByUser(User user);
}
