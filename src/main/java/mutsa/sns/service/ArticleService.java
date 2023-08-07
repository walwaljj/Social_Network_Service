package mutsa.sns.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.sns.domain.dto.article.ArticleResponseDto;
import mutsa.sns.domain.entity.ArticleEntity;
import mutsa.sns.domain.entity.UserEntity;
import mutsa.sns.exception.CustomException;
import mutsa.sns.exception.ErrorCode;
import mutsa.sns.repository.ArticleRepository;
import mutsa.sns.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;

    public ArticleResponseDto write(String username , List<MultipartFile> image,String title, String content) throws IOException {

        // user 이름으로 userEntity 찾기
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND, username));

        // articleRequestDto 를 바탕으로 ArticleEntity 생성
        ArticleEntity articleEntity = ArticleEntity.builder()
                .userId(userEntity)
                .title(title)
                .content(content)
                .build();

        ArticleEntity entity = articleRepository.save(articleEntity);

        // list 형태의 image 를 하나씩 업로드
        for (MultipartFile multipartFile : image) {
            imageService.uploadArticleImage(username, articleEntity.getId(), multipartFile);
        }

        return ArticleResponseDto.fromEntity(entity);
    }

    public List<ArticleEntity> findAllArticleByUserId(UserEntity userId){
        Optional<List<ArticleEntity>> optionalArticleList = articleRepository.findByUserId(userId);

        return optionalArticleList.orElseGet(null);
    }
}
