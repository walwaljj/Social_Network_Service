package mutsa.sns.service;

import lombok.RequiredArgsConstructor;
import mutsa.sns.domain.dto.article.ArticleResponseDto;
import mutsa.sns.domain.dto.follow.FollowResponseDto;
import mutsa.sns.domain.entity.ArticleEntity;
import mutsa.sns.domain.entity.FollowEntity;
import mutsa.sns.domain.entity.UserEntity;
import mutsa.sns.exception.CustomException;
import mutsa.sns.exception.ErrorCode;
import mutsa.sns.repository.ArticleRepository;
import mutsa.sns.repository.FollowRepository;
import mutsa.sns.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    /**
     * 팔로우
     */
    public FollowResponseDto follow(String username, Integer followingId) {

        Integer userId = userRepository.findByUsername(username).get().getId();

        UserEntity followingEntity = getFollowingUserEntity(followingId);

        // 해당 정보로 된 follow 생성
        FollowEntity followEntity = FollowEntity.builder()
                .userId(userId)
                .followingId(followingId)
                .build();

        followRepository.save(followEntity);

        return FollowResponseDto.toFollowDto(username, followingEntity.getUsername());
    }

    /**
     * 내가 팔로잉 하는 유저의 정보를 검증, 유저 정보 가져옴
     *
     * @param followingId
     * @return UserEntity
     */
    private UserEntity getFollowingUserEntity(Integer followingId) {

        UserEntity followingEntity = userRepository.findById(followingId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return followingEntity;
    }

    /**
     * 팔로우 해제 - 내가 팔로우 한 사용자 팔로우 취소
     */
    public void followCancel(String username, Integer followingId) {

        Integer userId = userRepository.findByUsername(username).get().getId();

        FollowEntity followEntity = followRepository.findByUserIdAndFollowingId(userId, followingId)
                .orElseThrow(() -> new CustomException(ErrorCode.FOLLOW_NOT_FOUND));

        followRepository.delete(followEntity);

    }

    /**
     * 팔로우한 모든 사람의 게시글 목록 조회
     */
    public List<ArticleResponseDto> followingUserArticleList(String username) {

        Integer userId = userRepository.findByUsername(username).get().getId();

        List<FollowEntity> followEntities = followRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.FOLLOW_NOT_FOUND));

        List<Integer> followingIdlist = followEntities.stream().map(FollowEntity::getFollowingId).toList();

        //반환할 새로운 리스트
        List<ArticleResponseDto> articleResponseDtoList = new ArrayList<>();

        for (Integer id : followingIdlist) {
            Optional<List<ArticleEntity>> articleEntityList = articleRepository.findByUserId(id);

            if (articleEntityList.isPresent()) {
                List<ArticleResponseDto> articleEntities = articleEntityList.get()
                        .stream().map(ArticleResponseDto::fromEntity).toList();
                articleResponseDtoList.addAll(articleEntities);
            }
        }

        return articleResponseDtoList;
    }

}
