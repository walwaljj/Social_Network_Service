package mutsa.sns.domain.dto.comment;

import lombok.Builder;
import lombok.Getter;
import mutsa.sns.domain.dto.article.ArticleResponseDto;
import mutsa.sns.domain.entity.ArticleEntity;
import mutsa.sns.domain.entity.CommentEntity;
import mutsa.sns.domain.entity.UserEntity;

@Builder
@Getter
public class CommentResponseDto {
    private String comment;
    private ArticleEntity article;
    private String username;

    public static CommentResponseDto fromEntity(CommentEntity entity){
        return CommentResponseDto.builder()
                .article(entity.getArticle())
                .comment(entity.getComment())
                .username(entity.getUsername())
                .build();
    }
}
