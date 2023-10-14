package mutsa.sns.repository;

import mutsa.sns.domain.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity , Integer> {
    List<LikeEntity> findByArticleId(Integer articleId);
    List<LikeEntity> findByUserId(Integer userId);
    Optional<LikeEntity> findByUserIdAndArticleId(Integer userId, Integer articleId);

}
