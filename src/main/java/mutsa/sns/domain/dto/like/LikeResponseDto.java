package mutsa.sns.domain.dto.like;

import jakarta.persistence.JoinColumn;
import lombok.Builder;
import lombok.Getter;
import mutsa.sns.domain.dto.comment.CommentResponseDto;
import mutsa.sns.domain.entity.ArticleEntity;
import mutsa.sns.domain.entity.LikeEntity;

@Builder
@Getter
public class LikeResponseDto {
    private ArticleEntity article;
    private Integer userId;
    private boolean status; // true == like
    public static LikeResponseDto fromEntity(LikeEntity entity){
        return LikeResponseDto.builder()
                .article(entity.getArticle())
                .userId(entity.getUserId())
                .status(true)
                .build();
    }
}
