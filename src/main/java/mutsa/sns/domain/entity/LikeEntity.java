package mutsa.sns.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Table(name = "Like_article")
@NoArgsConstructor
@AllArgsConstructor
public class LikeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "article_id")
    private ArticleEntity article;
    @JoinColumn(name = "user_id")
    private Integer userId;
    private boolean status; // true == like

    public boolean likeStatus() {
        return this.status;
    }





}
