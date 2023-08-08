package mutsa.sns.repository;

import mutsa.sns.domain.entity.ArticleEntity;
import mutsa.sns.domain.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity , Integer> {
    Optional<LikeEntity> findByArticle(ArticleEntity articleEntity);
}
