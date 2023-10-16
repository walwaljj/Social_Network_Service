package mutsa.sns.repository;

import mutsa.sns.domain.entity.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<FollowEntity, Integer> {
    Optional<FollowEntity> findByUserIdAndFollowingId(Integer userId, Integer followingId);

    Optional<List<FollowEntity>> findByUserId(Integer userId);
}
