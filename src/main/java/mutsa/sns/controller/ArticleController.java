package mutsa.sns.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.sns.domain.dto.article.ArticleResponseDto;
import mutsa.sns.domain.dto.like.LikeResponseDto;
import mutsa.sns.service.ArticleService;
import mutsa.sns.service.LikeService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;
    private final LikeService likeService;

    /**
     * 게시글 등록
     */
    @PostMapping(value = "/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ArticleResponseDto write(Authentication auth,
                                    @RequestParam(value = "image", required = false) List<MultipartFile> image,
                                    @RequestParam String title,
                                    @RequestParam String content) throws IOException {

        return articleService.write(auth.getName(), image, title, content);
    }

    /**
     * 게시글 조회
     */
    @GetMapping("/read/{username}/{articleId}")
    public ArticleResponseDto read(@PathVariable(value = "username") String username,
                                   @PathVariable(value = "articleId") Integer articleId) {

        return articleService.readArticle(username, articleId);
    }

    /**
     * 특정 user 의 모든 게시글 읽기 - username 으로 조회
     */
    @GetMapping("/read/{username}")
    public List<ArticleResponseDto> readSaleItemAll(@PathVariable(value = "username") String username) {
        return articleService.readAll(username);
    }

    /**
     * 게시글 수정
     */
    @PutMapping(value = "/{articleId}/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ArticleResponseDto update(@PathVariable(value = "articleId") Integer articleId,
                                     Authentication auth,
                                     @RequestParam(value = "image", required = false) List<MultipartFile> image,
                                     @RequestParam String title,
                                     @RequestParam String content) throws IOException {

        return articleService.updateArticle(articleId, auth.getName(), title, content, image);
    }

    /**
     * 게시글 삭제
     */
    @DeleteMapping("/{articleId}/delete")
    public void delete(Authentication auth,
                       @PathVariable(value = "articleId") Integer articleId) throws IOException {

        articleService.delete(auth.getName(), articleId);
    }

    /**
     * 해당 게시글을 좋아한 유저 목록 보기
     */
    @GetMapping(value = "/{articleId}/like-user")
    public List<LikeResponseDto> likeUserList(@PathVariable(value = "articleId") Integer articleId) {
        return likeService.likeUserList(articleId);
    }

}
