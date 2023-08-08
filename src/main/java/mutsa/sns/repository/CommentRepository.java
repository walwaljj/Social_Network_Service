package mutsa.sns.repository;

import mutsa.sns.domain.entity.ArticleEntity;
import mutsa.sns.domain.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity , Integer> {

    Optional<List<CommentEntity>> findByArticle(ArticleEntity articleEntity);
}
