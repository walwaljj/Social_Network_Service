package mutsa.sns.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
//    @ManyToOne
    @JoinColumn(name = "user_id")
    private Integer userId;
    private String title;
    private String content;
    private boolean draft;
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "article")
    @Setter
    private List<CommentEntity> comments;

    @OneToMany(mappedBy = "article")
    @Setter
    @JsonIgnore
    private List<ArticleImageEntity> articleImageUrlList;

}
