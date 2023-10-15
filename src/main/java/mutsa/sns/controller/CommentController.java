package mutsa.sns.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.sns.response.CommonResponse;
import mutsa.sns.response.ResponseCode;
import mutsa.sns.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{articleId}/comments")
    public ResponseEntity<CommonResponse> save(@PathVariable Integer articleId,
                                   @RequestParam("comment") String comment, Authentication auth){

        ResponseCode commentCreate = ResponseCode.COMMENT_CREATE;

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .responseCode(commentCreate)
                        .code(commentCreate.getCode())
                        .message(commentCreate.getMessage())
                        .data(commentService.save(articleId , comment, auth.getName()))
                        .build()
        );
    }

    @DeleteMapping("/{articleId}/comments/{commentsId}")
    public ResponseEntity<CommonResponse> delete(@PathVariable Integer articleId,
                                   @PathVariable("commentsId") Integer commentsId, Authentication auth){

        ResponseCode commentDelete = ResponseCode.COMMENT_DELETE;

        commentService.deleteComment(articleId , commentsId, auth.getName());

        return ResponseEntity.ok(
                CommonResponse.builder()
                        .responseCode(commentDelete)
                        .code(commentDelete.getCode())
                        .message(commentDelete.getMessage())
                        .build()
        );
    }


}
