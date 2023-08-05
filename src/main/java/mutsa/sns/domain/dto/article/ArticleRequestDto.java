package mutsa.sns.domain.dto.article;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import mutsa.sns.domain.entity.ArticleImageEntity;
import org.hibernate.annotations.Type;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ArticleRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String content;

    private List<String> articleImageUrlList;
}
