package mutsa.sns.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.sns.response.CommonResponse;
import mutsa.sns.response.ResponseCode;
import mutsa.sns.service.ArticleService;
import mutsa.sns.service.LikeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/articles")
public class ArticleController {
    private final ArticleService articleService;
    private final LikeService likeService;

    /**
     * 게시글 등록
     */
    @Operation(summary = "게시글 등록", description = "게시글 등록")
    @PostMapping(value = "/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse> write(Authentication auth,
                                                                  @RequestParam(value = "image", required = false) List<MultipartFile> image,
                                                                  @RequestParam String title,
                                                                  @RequestParam String content) throws IOException {
        ResponseCode articleCreate = ResponseCode.ARTICLE_CREATE;

        articleService.write(auth.getName(), image, title, content);

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .responseCode(articleCreate)
                        .code(articleCreate.getCode())
                        .message(articleCreate.getMessage())
                        .build()
        );
    }

    /**
     * 게시글 조회
     */
    @Operation(summary = "게시글 조회", description = "게시글 조회")
    @GetMapping("/read/{username}/{articleId}")
    public ResponseEntity<CommonResponse> read(@PathVariable(value = "username") String username,
                                   @PathVariable(value = "articleId") Integer articleId) {

        ResponseCode articleRead = ResponseCode.ARTICLE_READ;

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .responseCode(articleRead)
                        .code(articleRead.getCode())
                        .message(articleRead.getMessage())
                        .data(articleService.readArticle(username, articleId))
                        .build());
    }

    /**
     * 특정 user 의 모든 게시글 읽기 - username 으로 조회
     */
    @Operation(summary = "특정 user의 모든 게시글 읽기 ", description = "특정 user의 모든 게시글 읽기 ")
    @GetMapping("/read/{username}")
    public ResponseEntity<CommonResponse> readSaleItemAll(@PathVariable(value = "username") String username) {

        ResponseCode articleRead = ResponseCode.ARTICLE_READ;

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .responseCode(articleRead)
                        .code(articleRead.getCode())
                        .message(articleRead.getMessage())
                        .data(articleService.readAll(username))
                        .build());
    }

    /**
     * 게시글 수정
     */
    @Operation(summary = "게시글 수정", description = "게시글 수정")
    @PutMapping(value = "/{articleId}/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse> update(@PathVariable(value = "articleId") Integer articleId,
                                     Authentication auth,
                                     @RequestParam(value = "image", required = false) List<MultipartFile> image,
                                     @RequestParam String title,
                                     @RequestParam String content) throws IOException {

        ResponseCode articleUpdate = ResponseCode.ARTICLE_UPDATE;

        articleService.updateArticle(articleId, auth.getName(), title, content, image);

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .responseCode(articleUpdate)
                        .code(articleUpdate.getCode())
                        .message(articleUpdate.getMessage())
                        .build());
    }

    /**
     * 게시글 삭제
     */
    @Operation(summary = "게시글 삭제", description = "게시글 삭제")
    @DeleteMapping("/{articleId}/delete")
    public ResponseEntity<CommonResponse> delete(Authentication auth,
                       @PathVariable(value = "articleId") Integer articleId) throws IOException {

        ResponseCode articleDelete = ResponseCode.ARTICLE_DELETE;

        articleService.delete(auth.getName(), articleId);

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .responseCode(articleDelete)
                        .code(articleDelete.getCode())
                        .message(articleDelete.getMessage())
                        .build());
    }

    /**
     * 해당 게시글을 좋아한 유저 목록 보기
     */
    @Operation(summary = "게시글을 좋아한 유저 목록", description = "게시글을 좋아한 유저 목록")
    @GetMapping(value = "/{articleId}/like-user")
    public ResponseEntity<CommonResponse> likeUserList(@PathVariable(value = "articleId") Integer articleId) {

        ResponseCode articleLikesRead = ResponseCode.ARTICLE_LIKES_READ;

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .responseCode(articleLikesRead)
                        .code(articleLikesRead.getCode())
                        .message(articleLikesRead.getMessage())
                        .data(likeService.likeUserList(articleId))
                        .build());
    }

}
