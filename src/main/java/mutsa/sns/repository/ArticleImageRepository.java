package mutsa.sns.repository;

import mutsa.sns.domain.entity.ArticleImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleImageRepository extends JpaRepository<ArticleImageEntity, Integer> {
}
