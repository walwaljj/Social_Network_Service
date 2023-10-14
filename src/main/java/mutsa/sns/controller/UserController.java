package mutsa.sns.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.sns.domain.dto.article.ArticleResponseDto;
import mutsa.sns.domain.dto.like.LikeResponseDto;
import mutsa.sns.domain.dto.user.UserRequestDto;
import mutsa.sns.domain.dto.user.UserResponseDto;
import mutsa.sns.security.jwt.JwtRequestDto;
import mutsa.sns.security.jwt.JwtResponseDto;
import mutsa.sns.service.LikeService;
import mutsa.sns.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
    public UserResponseDto sign(@Valid @RequestBody UserRequestDto userDto) {
        return userService.sign(userDto);
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public JwtResponseDto login(@RequestBody JwtRequestDto userDto) {
        return userService.login(userDto);
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
    public UserResponseDto userProfile(Authentication auth) {
        log.info("getPrincipal = {}", auth.getPrincipal().toString());
        log.info("name = {}", auth.getName());

        return userService.findByUserName(auth.getName());
    }

    /**
     * 자신의 프로필 사진 업로드하기
     */
    @PutMapping(value = "/profile/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserResponseDto updateProfileImage(@RequestBody MultipartFile image, Authentication auth) throws IOException {
        log.info("updateProfileImage().auth.getName() = {} ", auth.getName());
        return userService.updateProfileImage(image, auth.getName());

    }

    /**
     * 자신이 좋아요 한 게시글 살펴보기
     */
    @GetMapping("/likes")
    public List<ArticleResponseDto> userLikeList(Authentication auth) {
        return likeService.userLikesList(auth.getName());
    }


    /**
     * 게시글 좋아요
     */
    @PostMapping("/articles/{articleId}/likes")
    public LikeResponseDto userAddLike(@PathVariable Integer articleId, Authentication auth) {
        return likeService.userAddLike(articleId, auth.getName());
    }


    /**
     * 게시글 싫어요
     */
    @DeleteMapping("/articles/{articleId}/likes")
    public void userDeleteLike(@PathVariable Integer articleId, Authentication auth) {
        likeService.userCancelLike(articleId, auth.getName());
    }

}
