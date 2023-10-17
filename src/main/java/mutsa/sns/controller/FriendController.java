package mutsa.sns.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.sns.response.CommonResponse;
import mutsa.sns.response.ResponseCode;
import mutsa.sns.service.FollowService;
import mutsa.sns.service.FriendService;
import mutsa.sns.service.LikeService;
import mutsa.sns.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/users/friends")
@RequiredArgsConstructor
@Slf4j
public class FriendController {

    private final FriendService friendService;

    /**
     * 친구 신청
     */
    @Operation(summary = "친구 신청", description = "친구 신청")
    @PostMapping("/recipient/{recipientId}")
    public ResponseEntity<CommonResponse> friendRequest(Authentication auth, @PathVariable Integer recipientId) {

        ResponseCode userFriendRequest = ResponseCode.USER_FRIEND_REQUEST;

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .responseCode(userFriendRequest)
                        .code(userFriendRequest.getCode())
                        .data(friendService.friendRequest(auth.getName(), recipientId))
                        .message(userFriendRequest.getMessage())
                        .build()
        );
    }

    /**
     * 친구 수락
     */
    @Operation(summary = "친구 수락", description = "친구 수락")
    @PutMapping("/recipient/{acceptId}/accept")
    public ResponseEntity<CommonResponse> friendAccept(Authentication auth, @PathVariable Integer acceptId) {

        ResponseCode friendAccept = ResponseCode.USER_FRIEND_REQUEST_ACCEPT;

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .responseCode(friendAccept)
                        .code(friendAccept.getCode())
                        .data(friendService.friendAccept(auth.getName(), acceptId))
                        .message(friendAccept.getMessage())
                        .build()
        );
    }

    /**
     * 친구 거절
     */
    @Operation(summary = "친구 거절", description = "친구 거절")
    @PutMapping("/recipient/{refuseId}/refuse")
    public ResponseEntity<CommonResponse> friendRefuse(Authentication auth, @PathVariable Integer refuseId) {

        ResponseCode userFriendRequestRefuse = ResponseCode.USER_FRIEND_REQUEST_REFUSE;

        friendService.friendRefuse(auth.getName(), refuseId);

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .responseCode(userFriendRequestRefuse)
                        .code(userFriendRequestRefuse.getCode())
                        .message(userFriendRequestRefuse.getMessage())
                        .build()
        );
    }

    /**
     * 친구 취소 - 친구 관계 취소
     */
    @Operation(summary = "친구 취소 ", description = "친구 취소 - 친구 관계 취소")
    @DeleteMapping("/{cancelId}/cancel")
    public ResponseEntity<CommonResponse> friendCancel(Authentication auth, @PathVariable Integer cancelId) {

        ResponseCode userFriendCancel = ResponseCode.USER_FRIEND_CANCEL;

        friendService.friendCancel(auth.getName(), cancelId);

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .responseCode(userFriendCancel)
                        .code(userFriendCancel.getCode())
                        .message(userFriendCancel.getMessage())
                        .build()
        );
    }

    /**
     * 친구 요청 조회
     */
    @Operation(summary = "친구 요청 조회", description = "친구 요청 조회")
    @GetMapping("/request-list")
    public ResponseEntity<CommonResponse> friendRequestList(Authentication auth) {

        ResponseCode userFriendRequest = ResponseCode.USER_FRIEND_REQUEST;

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .responseCode(userFriendRequest)
                        .code(userFriendRequest.getCode())
                        .data(friendService.friendRequestList(auth.getName()))
                        .message(userFriendRequest.getMessage())
                        .build()
        );
    }

    /**
     * 친구 목록 조회
     */
    @Operation(summary = "친구 목록 조회", description = "친구 목록 조회")
    @GetMapping("list")
    public ResponseEntity<CommonResponse> friendList(Authentication auth) {

        ResponseCode userFriendsList = ResponseCode.USER_FRIENDS_LIST;

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .responseCode(userFriendsList)
                        .code(userFriendsList.getCode())
                        .data(friendService.friendList(auth.getName()))
                        .message(userFriendsList.getMessage())
                        .build()
        );
    }

    /**
     * 친구의 모든 게시글 조회
     */
    @Operation(summary = "친구들의 게시글 전체 조회", description = "친구들의 게시글 전체 조회")
    @GetMapping("/articles")
    public ResponseEntity<CommonResponse> friendArticleList(Authentication auth) {

        ResponseCode userFriendArticleList = ResponseCode.USER_FRIEND_ARTICLE_LIST;

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .responseCode(userFriendArticleList)
                        .code(userFriendArticleList.getCode())
                        .data(friendService.friendArticleList(auth.getName()))
                        .message(userFriendArticleList.getMessage())
                        .build()
        );
    }
}
