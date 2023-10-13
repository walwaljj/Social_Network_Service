package mutsa.sns.repository;

import mutsa.sns.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity , Integer> {
    Optional<UserEntity> findByUsername(String username);
    boolean existsByUsername(String username);
}
