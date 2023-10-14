package mutsa.sns.domain.dto.article;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mutsa.sns.domain.entity.ArticleImageEntity;

import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private List<ArticleImageEntity> articleImageUrlList;
}
