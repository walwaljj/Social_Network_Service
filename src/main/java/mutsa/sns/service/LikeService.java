package mutsa.sns.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.sns.domain.dto.like.LikeResponseDto;
import mutsa.sns.domain.entity.ArticleEntity;
import mutsa.sns.domain.entity.LikeEntity;
import mutsa.sns.exception.CustomException;
import mutsa.sns.exception.ErrorCode;
import mutsa.sns.repository.ArticleRepository;
import mutsa.sns.repository.LikeRepository;
import mutsa.sns.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public LikeResponseDto like(String username, Integer articleId){

        ArticleEntity articleEntity = articleRepository.findById(articleId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        Optional<LikeEntity> optionalLike = likeRepository.findByArticle(articleEntity);

        if(optionalLike.isPresent()){
            LikeEntity likeEntity = optionalLike.get();
            boolean status = likeEntity.likeStatus();
            likeEntity.setStatus(!status);
            return LikeResponseDto.fromEntity(likeRepository.save(likeEntity));
        }

        LikeEntity likeEntity = new LikeEntity();
        likeEntity.setArticle(articleEntity);
        likeEntity.setUserId(userRepository.findByUsername(username).get().getId());
        likeEntity.setStatus(true);
        return LikeResponseDto.fromEntity(likeRepository.save(likeEntity));
    }
}
