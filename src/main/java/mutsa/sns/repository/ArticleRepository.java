package mutsa.sns.repository;

import mutsa.sns.domain.entity.ArticleEntity;
import mutsa.sns.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository < ArticleEntity ,Integer > {
    Optional<List<ArticleEntity>> findByUserId(UserEntity userId);
}
