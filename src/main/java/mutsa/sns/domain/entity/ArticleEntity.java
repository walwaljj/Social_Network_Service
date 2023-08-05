package mutsa.sns.domain.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;



@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "articles")
public class ArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userId;
    private String title;
    private String content;
    private boolean draft;
    private LocalDateTime deletedAt;
    @OneToMany(mappedBy = "article")
    @Nullable
    @Setter
    private List<ArticleImageEntity> articleImageUrlList;

}
