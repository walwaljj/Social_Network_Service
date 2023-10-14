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
import mutsa.sns.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

        // 게시물 저장.
        ArticleEntity articleSave = articleRepository.save(ArticleEntity.builder()
                .userId(userEntity.getId())
                .title(title)
                .content(content)
                .build()
        );

        List<ArticleImageEntity> articleImageUrlList = new ArrayList<>();

        // imagePath 를 저장할 변수
        String imagePath;

        // 이미지가 없다면
        if (image == null) {
            imagePath = imageService.uploadArticleImage(username, articleSave.getId(), null).toString();
            log.info("imagePath={}", imagePath);
            articleImageUrlList.add(ArticleImageEntity.builder().article(articleSave).imageUrl(imagePath).build());
            log.info("articleImageUrlList = {}", articleImageUrlList.get(0).getImageUrl());
        } else {
            // list 형태의 image 를 하나씩 업로드
            for (MultipartFile multipartFile : image) {
                imagePath = imageService.uploadArticleImage(username, articleSave.getId(), multipartFile).toString();
                articleImageUrlList.add(ArticleImageEntity.fromFileName(imagePath));
            }
        }

        articleSave.setArticleImageUrlList(articleImageUrlList);
        log.info("articleImageUrlList = {}", articleSave.getArticleImageUrlList().get(0).getImageUrl());
        return ArticleResponseDto.fromEntity(articleRepository.save(articleSave));
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

    /**
     * 수정
     */
    public ArticleResponseDto updateArticle(Integer articleId, String username, String title, String content, List<MultipartFile> image) throws IOException {

        ArticleEntity articleEntity = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        UserEntity userEntity = userRepository.findById(articleEntity.getUserId()).get();

        if(!userEntity.getUsername().equals(username)){
            throw new CustomException(ErrorCode.INVALID_PERMISSION);
        }

        List<ArticleImageEntity> articleImageUrlList = articleEntity.getArticleImageUrlList();

        String imagePath;

        // 변경할 이미지가 있다면
        if (image != null) {
            for (MultipartFile multipartFile : image) {
                imagePath = imageService.uploadArticleImage(username, articleEntity.getId(), multipartFile).toString();
                if(articleImageUrlList.get(0).getImageUrl().contains("article_"+articleId+"/default.png")){
                    articleImageUrlList.remove(0);
                }
                articleImageUrlList.add(ArticleImageEntity.fromFileName(imagePath));
            }
        }

        articleEntity.setArticleImageUrlList(articleImageUrlList);

        return ArticleResponseDto.fromEntity(articleRepository.save(articleEntity));
    }
}
