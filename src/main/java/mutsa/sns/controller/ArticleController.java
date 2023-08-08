package mutsa.sns.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.sns.domain.dto.article.ArticleRequestDto;
import mutsa.sns.domain.dto.article.ArticleResponseDto;
import mutsa.sns.domain.dto.like.LikeResponseDto;
import mutsa.sns.exception.CustomException;
import mutsa.sns.exception.ErrorCode;
import mutsa.sns.service.ArticleService;
import mutsa.sns.service.LikeService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;
    private final LikeService likeService;

    @PostMapping(value ="/{username}/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ArticleResponseDto write(@PathVariable String username, Authentication auth,
                                    @RequestParam(value = "image", required = false)  List<MultipartFile> image,
                                    @RequestParam String title,
                                    @RequestParam String content) throws IOException {

        if(!auth.getName().matches(username)){
            log.info("auth.getName() = {} ,username = {}",auth.getName(),username );
            throw new CustomException(ErrorCode.INVALID_PERMISSION);
        }

        return articleService.write(username, image, title , content);
    }

    @GetMapping("/read/{username}/{articleId}")
    public ArticleResponseDto read(@PathVariable(value = "username") String username,
                                   @PathVariable(value = "articleId") Integer articleId){

        return articleService.readArticle(username, articleId);
    }

    @GetMapping("/read/{username}")
    public List<ArticleResponseDto> readSaleItemAll(@PathVariable(value = "username") String username){
        return articleService.readAll(username);
    }

    @DeleteMapping("/delete/{username}/{articleId}")
    public void delete(@PathVariable String username,Authentication auth,
                        @PathVariable(value = "articleId") Integer articleId) throws IOException {

        if(!auth.getName().matches(username)){
            log.info("auth.getName() = {} ,username = {}",auth.getName(),username );
            throw new CustomException(ErrorCode.INVALID_PERMISSION);
        }

        articleService.delete(username, articleId);
    }

    @PutMapping("/read/{username}/{articleId}/like")
    public LikeResponseDto like(@PathVariable String username,Authentication auth,
                                @PathVariable(value = "articleId") Integer articleId){

        if(auth.getName().matches(username)){
            throw new CustomException(ErrorCode.INVALID_PERMISSION);
        }

        return likeService.like(auth.getName(), articleId);
    }

}
