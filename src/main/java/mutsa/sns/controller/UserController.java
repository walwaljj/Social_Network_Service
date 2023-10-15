package mutsa.sns.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.sns.domain.dto.user.UserRequestDto;
import mutsa.sns.response.CommonResponse;
import mutsa.sns.response.ResponseCode;
import mutsa.sns.security.jwt.JwtRequestDto;
import mutsa.sns.service.LikeService;
import mutsa.sns.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final LikeService likeService;

    /**
     * 회원가입
     */
    @PostMapping("/sign")
    public ResponseEntity<CommonResponse> sign(@Valid @RequestBody UserRequestDto userDto) {

        ResponseCode userCreate = ResponseCode.USER_CREATE;

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .responseCode(userCreate)
                        .code(userCreate.getCode())
                        .message(userCreate.getMessage())
                        .data(userService.sign(userDto))
                        .build()
        );
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(@RequestBody JwtRequestDto userDto) {

        ResponseCode userCreate = ResponseCode.USER_LOGIN;

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .responseCode(userCreate)
                        .code(userCreate.getCode())
                        .message(userCreate.getMessage())
                        .data(userService.login(userDto))
                        .build()
        );
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public void logout(Authentication auth, HttpServletResponse response) {
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }


    /**
     * 자신의 프로필 보기
     */
    @GetMapping("/profile")
    public ResponseEntity<CommonResponse> userProfile(Authentication auth) {

        ResponseCode userProfileRead = ResponseCode.USER_PROFILE_READ;

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .responseCode(userProfileRead)
                        .code(userProfileRead.getCode())
                        .message(userProfileRead.getMessage())
                        .data(userService.findByUserName(auth.getName()))
                        .build()
        );
    }

    /**
     * 자신의 프로필 사진 업로드하기
     */
    @PutMapping(value = "/profile/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse> updateProfileImage(@RequestBody MultipartFile image, Authentication auth) throws IOException {

        ResponseCode userProfileUpdate = ResponseCode.USER_PROFILE_UPDATE;

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .responseCode(userProfileUpdate)
                        .code(userProfileUpdate.getCode())
                        .message(userProfileUpdate.getMessage())
                        .data(userService.findByUserName(auth.getName()))
                        .build()
        );

    }

    /**
     * 자신이 좋아요 한 게시글 살펴보기
     */
    @GetMapping("/likes")
    public ResponseEntity<CommonResponse> userLikeList(Authentication auth) {

        ResponseCode userLikesRead = ResponseCode.USER_LIKES_READ;

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .responseCode(userLikesRead)
                        .code(userLikesRead.getCode())
                        .message(userLikesRead.getMessage())
                        .data(likeService.userLikesList(auth.getName()))
                        .build()
        );
    }


    /**
     * 게시글 좋아요
     */
    @PostMapping("/articles/{articleId}/likes")
    public ResponseEntity<CommonResponse> userAddLike(@PathVariable Integer articleId, Authentication auth) {

        ResponseCode userLikesArticle = ResponseCode.USER_LIKES_ARTICLE;

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .responseCode(userLikesArticle)
                        .code(userLikesArticle.getCode())
                        .message(userLikesArticle.getMessage())
                        .data(likeService.userAddLike(articleId, auth.getName()))
                        .build()
        );
    }


    /**
     * 게시글 좋아요 취소
     */
    @DeleteMapping("/articles/{articleId}/likes")
    public ResponseEntity<CommonResponse> userDeleteLike(@PathVariable Integer articleId, Authentication auth) {

        ResponseCode likesCancelArticle = ResponseCode.USER_LIKES_CANCEL_ARTICLE;

        likeService.userCancelLike(articleId, auth.getName());

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .responseCode(likesCancelArticle)
                        .code(likesCancelArticle.getCode())
                        .message(likesCancelArticle.getMessage())
                        .build()
        );
    }

}
