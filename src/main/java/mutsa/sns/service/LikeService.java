package mutsa.sns.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.sns.domain.dto.article.ArticleResponseDto;
import mutsa.sns.domain.dto.like.LikeResponseDto;
import mutsa.sns.domain.entity.ArticleEntity;
import mutsa.sns.domain.entity.LikeEntity;
import mutsa.sns.domain.entity.UserEntity;
import mutsa.sns.exception.CustomException;
import mutsa.sns.exception.ErrorCode;
import mutsa.sns.repository.ArticleRepository;
import mutsa.sns.repository.LikeRepository;
import mutsa.sns.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    /**
     * 좋아요
     */
    public LikeResponseDto userAddLike(Integer articleId, String username) {

        // 게시글 찾기
        ArticleEntity articleEntity = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));

        // 사용자 찾기
        UserEntity userEntity = userRepository.findByUsername(username).get();

        // 좋아요 정보 찾기 (이미 정보가 있다면 LIKES_ALREADY_EXISTS)
        if (likeRepository.findByUserIdAndArticleId(userEntity.getId(), articleEntity.getId()).isPresent()) {
            throw new CustomException(ErrorCode.LIKES_ALREADY_EXISTS);
        }

        LikeEntity likeEntity = likeRepository.save(LikeEntity.builder()
                .articleId(articleEntity.getId())
                .userId(userEntity.getId())
                .build());

        return LikeResponseDto.fromEntity(likeEntity);
    }

    /**
     * 좋아요 취소
     */
    public void userCancelLike(Integer articleId, String username) {
        // 게시글 찾기
        ArticleEntity articleEntity = articleRepository.findById(articleId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        // 사용자 찾기
        UserEntity userEntity = userRepository.findByUsername(username).get();

        // 좋아요 정보 찾기 (정보가 없다면 LIKES_NOT_FOUND)
        LikeEntity likeEntity = likeRepository.findByUserIdAndArticleId(userEntity.getId(), articleEntity.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.LIKES_NOT_FOUND));

        likeRepository.delete(likeEntity);
    }

    /**
     * 해당 유저가 좋아요를 누른 게시글 목록을 반환하는 메서드.
     */
    public List<ArticleResponseDto> userLikesList(String username) {

        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<LikeEntity> likeEntityList = likeRepository.findByUserId(userEntity.getId());

        List<ArticleResponseDto> articleResponseDtoList = new ArrayList<>();

        for (LikeEntity likeEntity : likeEntityList) {
            Integer articleId = likeEntity.getArticleId();
            articleResponseDtoList.add(ArticleResponseDto.fromEntity(articleRepository.findById(articleId).get()));
        }

        return articleResponseDtoList;
    }

    /**
     * 좋아요한 유저 보기
     */
    public List<LikeResponseDto> likeUserList(Integer articleId) {
        return likeRepository.findByArticleId(articleId).stream().map(LikeResponseDto::fromEntity).toList();
    }
}
