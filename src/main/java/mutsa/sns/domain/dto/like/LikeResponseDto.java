package mutsa.sns.domain.dto.like;

import lombok.Builder;
import lombok.Getter;
import mutsa.sns.domain.entity.LikeEntity;

@Builder
@Getter
public class LikeResponseDto {

    private Integer articleId;
    private Integer userId;
    public static LikeResponseDto fromEntity(LikeEntity entity){
        return LikeResponseDto.builder()
                .articleId(entity.getArticleId())
                .userId(entity.getUserId())
                .build();
    }
}
