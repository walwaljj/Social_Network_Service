package mutsa.sns.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.sns.response.CommonResponse;
import mutsa.sns.response.ResponseCode;
import mutsa.sns.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/users/follow")
@RequiredArgsConstructor
@Slf4j
public class FollowController {

    private final FollowService followService;


    /**
     * 팔로우한 사용자 모든 게시글 조회
     */
    @Operation(summary = "사용자가 팔로우한 사용자들의 게시글 조회", description = "사용자가 팔로우한 사용자들의 게시글 조회")
    @GetMapping("/articles")
    public ResponseEntity<CommonResponse> followArticleList(Authentication auth) {

        ResponseCode userFollowArticleList = ResponseCode.USER_FOLLOW_ARTICLE_LIST;

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .responseCode(userFollowArticleList)
                        .code(userFollowArticleList.getCode())
                        .data(followService.followingUserArticleList(auth.getName()))
                        .message(userFollowArticleList.getMessage())
                        .build()
        );
    }

    /**
     * 내가 팔로우 한 사용자 팔로우 취소
     */
    @Operation(summary = "팔로우 취소", description = "팔로우 취소")
    @DeleteMapping("/{cancelId}/cancel")
    public ResponseEntity<CommonResponse> followArticleList(@PathVariable Integer cancelId, Authentication auth) {

        ResponseCode userFollowCancel = ResponseCode.USER_FOLLOW_CANCEL;

        followService.followCancel(auth.getName(), cancelId);

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .responseCode(userFollowCancel)
                        .code(userFollowCancel.getCode())
                        .message(userFollowCancel.getMessage())
                        .build()
        );
    }

    /**
     * 팔로우
     */
    @Operation(summary = "팔로우 신청", description = "팔로우 신청")
    @PostMapping("/{followId}")
    public ResponseEntity<CommonResponse> follow(@PathVariable Integer followId, Authentication auth) {

        ResponseCode userFollowRequest = ResponseCode.USER_FOLLOW_REQUEST;

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .responseCode(userFollowRequest)
                        .code(userFollowRequest.getCode())
                        .data(followService.follow(auth.getName(), followId))
                        .message(userFollowRequest.getMessage())
                        .build()
        );
    }

}
