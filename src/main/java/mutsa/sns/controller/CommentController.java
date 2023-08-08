package mutsa.sns.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.sns.domain.dto.comment.CommentResponseDto;
import mutsa.sns.exception.CustomException;
import mutsa.sns.exception.ErrorCode;
import mutsa.sns.service.CommentService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{articleId}/comments")
    public CommentResponseDto save(@PathVariable Integer articleId,
                                   @RequestParam("comment") String comment, Authentication auth){
        String username = auth.getName();
        return commentService.save(articleId , comment, username);
    }

    @DeleteMapping("/{articleId}/comments/{commentsId}")
    public void delete(@PathVariable Integer articleId,
                                   @PathVariable("commentsId") Integer commentsId, Authentication auth){
        String username = auth.getName();
        if(!auth.getName().matches(username)){
            log.info("auth.getName() = {} ,username = {}",auth.getName(),username );
            throw new CustomException(ErrorCode.INVALID_PERMISSION);
        }
        commentService.deleteComment(articleId , commentsId);
    }


}
