package mutsa.sns.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@Entity
@Table(name = "comments")
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "article_id")
    @JsonBackReference
    private ArticleEntity article;

    @JoinColumn(name = "user_id")
    private Integer userId;

    @Column(nullable = false)
    private String username;

}
