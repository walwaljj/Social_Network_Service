package mutsa.sns.domain.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import mutsa.sns.domain.entity.ArticleEntity;
import mutsa.sns.domain.entity.ArticleImageEntity;
import mutsa.sns.domain.entity.UserEntity;

import java.util.List;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleResponseDto {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private UserEntity user;
    private List<ArticleImageEntity> articleImageUrlList;

    public static ArticleResponseDto fromEntity(ArticleEntity entity){
        return ArticleResponseDto.builder()
                .title(entity.getTitle())
                .content(entity.getContent())
                .user(entity.getUserId())
                .articleImageUrlList(entity.getArticleImageUrlList())
                .build();
    }

}
