package mutsa.sns.domain.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import mutsa.sns.domain.entity.ArticleEntity;
import mutsa.sns.domain.entity.ArticleImageEntity;
import mutsa.sns.domain.entity.CommentEntity;
import mutsa.sns.domain.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleResponseDto {

    private Integer id;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private Integer userId;
    private Integer likeCount;
    private List<CommentEntity> comments;
    private List<String> articleImageUrlList;
    private String articleImageUrl;

    public static ArticleResponseDto fromEntity(ArticleEntity entity){
        return ArticleResponseDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .userId(entity.getUserId())
                .articleImageUrlList(imageUrlToString(entity.getArticleImageUrlList()))
                .build();
    }
    public static List<String> imageUrlToString(List<ArticleImageEntity> imageUrlList){
        List<String> stringImageUrlList = new ArrayList<>();

        if(imageUrlList.size() != 0){
            for (ArticleImageEntity articleEntity : imageUrlList) {
                stringImageUrlList.add(articleEntity.getImageUrl());
            }
        }
        return stringImageUrlList;
    }

    public static ArticleResponseDto showArticleList(ArticleResponseDto articleResponseDto, String articleImageUrl){

        return ArticleResponseDto.builder()
                .title(articleResponseDto.getTitle())
                .content(articleResponseDto.getContent())
                .userId(articleResponseDto.getUserId())
                .articleImageUrl(articleImageUrl).build();
    }

    public static ArticleResponseDto showArticle(ArticleResponseDto articleResponseDto){

        return ArticleResponseDto.builder()
                .title(articleResponseDto.getTitle())
                .content(articleResponseDto.getContent())
                .userId(articleResponseDto.getUserId())
                .articleImageUrlList(articleResponseDto.articleImageUrlList).build();
    }

}
