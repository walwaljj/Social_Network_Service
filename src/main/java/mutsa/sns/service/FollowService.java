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

        UserEntity followingUserEntity = getFollowingUserEntity(followingId);

        // follow 중복 검증
        Optional<List<FollowEntity>> userFollowList = followRepository.findByUserId(userId);

        if (userFollowList.isPresent()) { // 사용자의 팔로우 정보가 있다면
            for (FollowEntity followEntity : userFollowList.get()) {
                // 현재 팔로우 하려고 하는 id 와 같은지 확인
                if (followEntity.getFollowingId().equals(followingId)) {
                    // 같다면 중복 에러 발생
                    throw new CustomException(ErrorCode.DUPLICATION_POSSIBLE);
                }
            }
        }

        // follow DB 에 중복된 정보가 없다면
        // 해당 정보로 된 follow 생성
        FollowEntity followEntity = FollowEntity.builder()
                .userId(userId)
                .followingId(followingId)
                .build();

        followRepository.save(followEntity);

        return FollowResponseDto.toFollowDto(username, followingUserEntity.getUsername());
    }

    /**
     * 내가 팔로잉 하는 유저의 정보를 검증, 유저 정보 가져옴
     *
     * @param followingId
     * @return UserEntity
     */
    private UserEntity getFollowingUserEntity(Integer followingId) {

        UserEntity followingUserEntity = userRepository.findById(followingId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return followingUserEntity;
    }

    // 팔로우 중복 검증
    private boolean followingEntityIsEmpty(Integer userId, Integer followingId) {

        Optional<List<FollowEntity>> userFollowList = followRepository.findByUserId(userId);

        if (userFollowList.isPresent()) { // 사용자의 팔로우 정보가 있다면
            for (FollowEntity followEntity : userFollowList.get()) {
                // 현재 팔로우 하려고 하는 id 와 같은지 확인
                if (followEntity.getFollowingId().equals(followingId)) {
                    // 같다면 중복 에러 발생
                    throw new CustomException(ErrorCode.DUPLICATION_POSSIBLE);
                }
            }
        }
        return true;
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
