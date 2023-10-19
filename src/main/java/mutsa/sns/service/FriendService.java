package mutsa.sns.service;

import lombok.RequiredArgsConstructor;
import mutsa.sns.domain.dto.article.ArticleResponseDto;
import mutsa.sns.domain.dto.friend.FriendResponseDto;
import mutsa.sns.domain.entity.ArticleEntity;
import mutsa.sns.domain.entity.FriendEntity;
import mutsa.sns.domain.entity.FriendStatus;
import mutsa.sns.domain.entity.UserEntity;
import mutsa.sns.exception.CustomException;
import mutsa.sns.exception.ErrorCode;
import mutsa.sns.repository.ArticleRepository;
import mutsa.sns.repository.FriendRepository;
import mutsa.sns.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final ArticleRepository articleRepository;

    /**
     * 친구 신청
     */
    public FriendResponseDto friendRequest(String username, Integer recipientId) {

        UserEntity senderUserEntity = userRepository.findByUsername(username).get();

        userCheck(senderUserEntity.getId(), recipientId);

        UserEntity recipientEntity = getUserEntity(recipientId);

        if (isFriendRequestReceive(senderUserEntity, recipientEntity)) {
            throw new CustomException(ErrorCode.DUPLICATION_POSSIBLE);
        }
        // 해당 정보로 된 친구 신청 생성
        friendRepository.save(FriendEntity.builder()
                .sender(senderUserEntity)
                .recipient(recipientEntity)
                .status(FriendStatus.REQUEST)
                .build());

        return FriendResponseDto.getFriendName(username, recipientEntity.getUsername(), FriendStatus.REQUEST);
    }

    /**
     * 자신에게 신청 방지
     */
    private static void userCheck(Integer loginUserId, Integer recipientId) {
        if (loginUserId.equals(recipientId)) {
            throw new CustomException(ErrorCode.NOT_POSSIBLE);
        }
    }

    /**
     * 친구 수락 -> 친구가 됨.
     *
     * @param loginUsername 로그인 한 사용자 이름
     * @param acceptId      친구 수락 할 id (친구 요청을 먼저 보낸 사용자 id)
     */
    public FriendResponseDto friendAccept(String loginUsername, Integer acceptId) {

        // 로그인한 사용자 찾기
        UserEntity loginUserEntity = userRepository.findByUsername(loginUsername).get();

        userCheck(loginUserEntity.getId(), acceptId);

        // 로그인한 사용자가 수락할 요청을 찾기
        // 요청자를 찾기
        UserEntity acceptEntity = getUserEntity(acceptId);

        // user가 recipientId 에게 받은 친구 요청이 없다면 throw
        if (!isFriendRequestReceive(loginUserEntity, acceptEntity)) {
            throw new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND);
        }

        // 있다면 user 아이디로 followEntity 생성 후 save
//        friendRepository.save(FriendEntity.builder()
//                .sender(loginUserEntity)
//                .recipient(acceptEntity)
//                .build());

        FriendEntity friendEntity = getFriendEntity(acceptEntity, loginUserEntity);
        friendEntity.statusUpdate(FriendStatus.ACCEPTED); // 수락으로 변경
        friendRepository.save(friendEntity);
//
//        // 친구 목록에 추가
//        loginUserEntity.getFriendList().add(friendEntity);
//        acceptEntity.getFriendList().add(friendEntity);

        return FriendResponseDto.getFriendName(loginUsername, acceptEntity.getUsername(), friendEntity.getStatus());
    }

    /**
     * 정보를 바탕으로 한 친구 신청 요청이 있는지 확인 boolean 리턴
     * 있다면 true
     *
     * @param sender    최초로 친구 신청을 한 User
     * @param recipient 친구 신청을 받은 User
     */
    private boolean isFriendRequestReceive(UserEntity sender, UserEntity recipient) {

        Optional<Set<FriendEntity>> allBySender = friendRepository.findAllBySender(recipient);

        if (allBySender.isEmpty()) {
            return false;
        } else {
            for (FriendEntity friendEntity : allBySender.get()) {
                if (friendEntity.getRecipient().equals(sender)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 정보를 바탕으로 한 친구 신청 요청이 있는지 확인
     *
     * @param sender    최초로 친구 신청을 한 User
     * @param recipient 친구 신청을 받은 User
     */
    private FriendEntity getFriendEntity(UserEntity sender, UserEntity recipient) {
        return friendRepository.findBySenderAndRecipient(sender, recipient)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

    }

    private UserEntity getUserEntity(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * 친구 신청 거절 - 친구 신청 요청을 거절.
     */
    public void friendRefuse(String username, Integer refuseId) {

        // userId 찾기
        UserEntity loginUserEntity = userRepository.findByUsername(username).get();

        // 요청자를 찾기
        UserEntity refuseEntity = getUserEntity(refuseId);

        // user가 recipientId 에게 받은 친구 요청이 없다면 throw
        if (!isFriendRequestReceive(refuseEntity, loginUserEntity)) {
            throw new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND);
        }

//        FriendEntity friendEntity = getFriendEntity(refuseEntity, loginUserEntity);
//        friendEntity.statusUpdate(FriendStatus.REJECTED); // 거절으로 변경

        // 있다면 user delete
        friendRepository.delete(getFriendEntity(refuseEntity, loginUserEntity));
    }

    /**
     * 친구 취소 - 친구 였던 관계를 끝냄
     */
    public void friendCancel(String username, Integer cancelId) {

        // userId 찾기
        UserEntity loginUserEntity = userRepository.findByUsername(username).get();

        // 취소할 유저를 찾기
        UserEntity cancleUserEntity = getUserEntity(cancelId);

        // user가 recipientId 에게 받은 친구 요청이 없다면 throw
        FriendEntity friendEntity = getFriendEntity(loginUserEntity, cancleUserEntity);

        // 삭제
        friendRepository.delete(friendEntity);

//        loginUserEntity.getFriendList().remove(cancleUserEntity);
//        cancleUserEntity.getFriendList().remove(loginUserEntity);

    }


    /**
     * 친구 요청 목록 조회
     */
    public List<FriendResponseDto> friendRequestList(String username) {

        // 로그인 한 user의 Id 찾기
        UserEntity loginUserEntity = userRepository.findByUsername(username).get();

        // 반환을 위한 List 생성
        List<FriendResponseDto> requestList = new ArrayList<>();

        // 사용자에게 친구 요청한 목록 반환
        List<FriendEntity> friendRequestList = friendRepository.findByRecipient(loginUserEntity)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        // 친구 요청 목록에서, 사용자에게 친구를 요청한 친구의 이름을 얻고, 반환할 List 에 저장.
        for (FriendEntity friendEntity : friendRequestList) {
            if (friendEntity.getStatus().equals(FriendStatus.REQUEST)) {
                UserEntity recipientEntity = getUserEntity(friendEntity.getSender().getId());
                requestList.add(FriendResponseDto.getFriendName(username, recipientEntity.getUsername(), FriendStatus.REQUEST));
            }
        }

        return requestList;
    }

    /**
     * 친구 목록 조회
     */
    public List<String> friendList(String username) {

        // 로그인 한 user의 Id 찾기
        UserEntity loginUserEntity = userRepository.findByUsername(username).get();

        List<String> friendNameList = new ArrayList<>();

        Optional<List<FriendEntity>> allByStatus = friendRepository.findAllByStatus(FriendStatus.ACCEPTED);

        if (allByStatus.isPresent()) {
            for (FriendEntity friendEntity : allByStatus.get()) {
                if (friendEntity.getRecipient().equals(loginUserEntity)) {
                    friendNameList.add(friendEntity.getSender().getUsername());
                }
                else if(friendEntity.getSender().equals(loginUserEntity)){
                    friendNameList.add(friendEntity.getSender().getUsername());
                }
            }
        }

        return friendNameList;
    }


    /**
     * 친구의 모든 게시글 조회
     */
    public List<ArticleResponseDto> friendArticleList(String username) {

        // 로그인 한 user의 Id 찾기
        UserEntity loginUserEntity = userRepository.findByUsername(username).get();

        List<ArticleEntity> friendArticleEntities = new ArrayList<>();

        // Sender 또는 Recipient 중 UserEntity 가 있는지 찾기
        Set<FriendEntity> resultSet = new HashSet<>();

        Optional<Set<FriendEntity>> allByRecipient = friendRepository.findAllByRecipient(loginUserEntity);

        if (allByRecipient.isPresent()) {
            resultSet.addAll(allByRecipient.get());
        }

        Optional<Set<FriendEntity>> allBySender = friendRepository.findAllBySender(loginUserEntity);

        if (allBySender.isPresent()) {
            resultSet.addAll(allByRecipient.get());
        }

        if (!resultSet.isEmpty()) {
            for (FriendEntity friendEntity : resultSet) {
                // 수락 상태 확인
                if (friendEntity.getStatus().equals(FriendStatus.ACCEPTED)) {
                    Optional<List<ArticleEntity>> articleEntityList =
                            articleRepository.findByUserId(friendEntity.getRecipient().getId());
                    // 게시글이 있다면 List에 넣기
                    if (articleEntityList.isPresent()) {
                        friendArticleEntities.addAll(articleEntityList.get());
                    }
                }
            }
        }

        return friendArticleEntities.stream().map(ArticleResponseDto::fromEntity).toList();
    }

}
