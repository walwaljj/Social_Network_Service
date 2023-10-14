package mutsa.sns.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import mutsa.sns.domain.dto.article.ArticleRequestDto;

import java.time.LocalDateTime;
import java.util.List;



@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "article")
public class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "user_id")
    private Integer userId;

    @Setter
    private String title;

    private String content;

    private boolean draft;

    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "article")
    @Setter
    private List<CommentEntity> comments;

    @OneToMany(mappedBy = "article")
    @Setter
    private List<ArticleImageEntity> articleImageUrlList;

}
