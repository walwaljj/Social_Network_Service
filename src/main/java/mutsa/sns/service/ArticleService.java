package mutsa.sns.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.sns.domain.dto.article.ArticleResponseDto;
import mutsa.sns.domain.entity.ArticleEntity;
import mutsa.sns.domain.entity.ArticleImageEntity;
import mutsa.sns.domain.entity.UserEntity;
import mutsa.sns.exception.CustomException;
import mutsa.sns.exception.ErrorCode;
import mutsa.sns.repository.ArticleImageRepository;
import mutsa.sns.repository.ArticleRepository;
import mutsa.sns.repository.CommentRepository;
import mutsa.sns.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final CommentService commentService;
    private final ArticleImageRepository articleImageRepository;

    public ArticleResponseDto write(String username, List<MultipartFile> image, String title, String content) throws IOException {

        // user 이름으로 userEntity 찾기
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, username));

        // articleRequestDto 를 바탕으로 ArticleEntity 생성
        ArticleEntity articleEntity = ArticleEntity.builder()
                .userId(userEntity.getId())
                .title(title)
                .content(content)
                .build();

        // 게시물 저장.
        ArticleEntity articleSave = articleRepository.save(articleEntity);

        List<ArticleImageEntity> articleImageUrlList = new ArrayList<>();

        // imagePath 를 저장할 변수
        String imagePath;

        // 이미지가 없다면
        if (image == null) {
            imagePath = imageService.uploadArticleImage(username, articleSave.getId(), null).toString();
            log.info("imagePath={}", imagePath);
            ArticleImageEntity articleImageEntity = ArticleImageEntity.fromFileName(imagePath);
            articleImageUrlList.add(articleImageEntity);
        } else {
            // list 형태의 image 를 하나씩 업로드
            for (MultipartFile multipartFile : image) {
                imagePath = imageService.uploadArticleImage(username, articleSave.getId(), multipartFile).toString();
                articleImageUrlList.add(ArticleImageEntity.fromFileName(imagePath));
            }
        }

        articleEntity.setArticleImageUrlList(articleImageUrlList);

        ArticleEntity entity = articleRepository.save(articleEntity);

        return ArticleResponseDto.fromEntity(entity);
    }

    public List<ArticleEntity> findAllArticleByUserId(UserEntity user) {
        Optional<List<ArticleEntity>> optionalArticleList = articleRepository.findByUserId(user.getId());

        return optionalArticleList.orElseGet(null);
    }

    public List<ArticleResponseDto> findAllArticlesByUsername(String username) {

        List<ArticleResponseDto> articleResponseList = new ArrayList<>();

        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, username));

        List<ArticleEntity> articleEntities = articleRepository.findByUserId(userEntity.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, username + "님의 글"));

        for (ArticleEntity articleEntity : articleEntities) {
            articleResponseList.add(ArticleResponseDto.builder()
                    .comments(articleEntity.getComments())
                    .id(articleEntity.getId())
                    .userId(userEntity.getId())
                    .articleImageUrlList(ArticleResponseDto.imageUrlToString(articleEntity.getArticleImageUrlList()))
                    .title(articleEntity.getTitle())
                    .content(articleEntity.getContent()).build());

        }
        return articleResponseList;
    }

    public ArticleResponseDto readArticle(String username, Integer articleId) {

        List<ArticleResponseDto> articleListByUsername = findAllArticlesByUsername(username);

        return articleListByUsername.stream().filter(article -> article.getId().equals(articleId)).findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, username + " 님의 " + articleId + "번째 글"));

    }

    public List<ArticleResponseDto> readAll(String username) {
        List<ArticleResponseDto> articleListByUsername = findAllArticlesByUsername(username);
        String imageUrl;
        List<ArticleResponseDto> articleList = new ArrayList<>();
        for (ArticleResponseDto articleResponseDto : articleListByUsername) {
            imageUrl = articleResponseDto.getArticleImageUrlList().stream().findFirst().get();

            articleList.add(ArticleResponseDto.showArticleList(articleResponseDto, imageUrl));
        }

        return articleList;

    }

    public void delete(String username, Integer articleId) throws IOException {
        ArticleEntity articleEntity = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, username + " 님의 " + articleId + "번째 글"));

        imageService.deleteImage(username, articleId);
        commentService.deleteAllComments(articleId);
        articleRepository.delete(articleEntity);

    }
}
