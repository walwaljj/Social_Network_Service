package mutsa.sns.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

@Builder
@Getter
@Entity
@Table(name = "article_images")
@NoArgsConstructor
@AllArgsConstructor
public class ArticleImageEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "article_id")
    private ArticleEntity article;
    @Setter
    private String imageUrl;

    public static ArticleImageEntity fromFileName(String fileName) {
        return ArticleImageEntity.builder()
                .imageUrl(fileName)
                .build();
    }

    public void updateArticle(ArticleEntity article){
        this.article = article;
    }
}
