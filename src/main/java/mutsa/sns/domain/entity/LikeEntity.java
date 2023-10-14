package mutsa.sns.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@Table(name = "Like_article")
@NoArgsConstructor
@AllArgsConstructor
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer articleId;

    @JoinColumn(name = "user_id")
    private Integer userId;

}
