package mutsa.sns.controller;

import lombok.RequiredArgsConstructor;
import mutsa.sns.domain.dto.article.ArticleRequestDto;
import mutsa.sns.domain.dto.article.ArticleResponseDto;
import mutsa.sns.exception.CustomException;
import mutsa.sns.exception.ErrorCode;
import mutsa.sns.service.ArticleService;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping(value ="/{username}/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @PreAuthorize("#username == new Authentication().getName()")
    public ArticleResponseDto write(@PathVariable String username, Authentication auth,
                                    @RequestParam List<MultipartFile> image,
                                    @RequestParam String title,
                                    @RequestParam String content) throws IOException {

        if(!auth.getName().equals(username)){
            new CustomException(ErrorCode.INVALID_PERMISSION);
        }
        return articleService.write(username, image, title , content);
    }

}
