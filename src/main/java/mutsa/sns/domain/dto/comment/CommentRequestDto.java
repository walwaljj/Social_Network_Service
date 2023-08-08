package mutsa.sns.domain.dto.comment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mutsa.sns.domain.entity.ArticleEntity;
import mutsa.sns.domain.entity.UserEntity;

@Builder
@Getter
public class CommentRequestDto {
    private String comment;
    private ArticleEntity article;
    private String username;



}
