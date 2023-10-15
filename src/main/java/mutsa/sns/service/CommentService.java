package mutsa.sns.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.sns.domain.dto.comment.CommentRequestDto;
import mutsa.sns.domain.dto.comment.CommentResponseDto;
import mutsa.sns.domain.entity.ArticleEntity;
import mutsa.sns.domain.entity.CommentEntity;
import mutsa.sns.domain.entity.UserEntity;
import mutsa.sns.exception.CustomException;
import mutsa.sns.exception.ErrorCode;
import mutsa.sns.repository.ArticleRepository;
import mutsa.sns.repository.CommentRepository;
import mutsa.sns.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public CommentResponseDto save(Integer articleId,String comment, String username){

        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, username));

        ArticleEntity articleEntity = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, String.format("%d 번째 글", articleId)));

        CommentEntity commentEntity = CommentEntity.builder()
                .username(userEntity.getUsername())
                .article(articleEntity)
                .comment(comment).build();

        return CommentResponseDto.fromEntity(commentRepository.save(commentEntity));
    }

    private List<CommentEntity> findCommentListByArticleId(Integer articleId){
        ArticleEntity articleEntity = articleRepository.findById(articleId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        log.info("articleId = {}" , articleId);
        if(!commentRepository.findByArticle(articleEntity).isPresent()){
            throw  new CustomException(ErrorCode.NOT_FOUND, "답글");
        }
        return commentRepository.findByArticle(articleEntity).get();
    }

    public void deleteAllComments(Integer articleId){
        List<CommentEntity> commentListByArticleId = findCommentListByArticleId(articleId);

        for (CommentEntity commentEntity : commentListByArticleId) {
            commentRepository.delete(commentEntity);

        }
    }

    public void deleteComment(Integer articleId, Integer commentsId, String username) {

        List<CommentEntity> commentListByArticleId = findCommentListByArticleId(articleId);

        for (CommentEntity commentEntity : commentListByArticleId) {
            //  삭제를 시도하는 사용자와 comment 작성자가 일치한지 확인.
            if(commentEntity.getUsername().equals(username)){
                throw new CustomException(ErrorCode.INVALID_PERMISSION);
            }
            // 삭제할 comment 의 Id 와 entity 의 id 를 확인.
            if(commentEntity.getId().equals(commentsId)) {
                commentRepository.delete(commentEntity);
                break;
            }
        }


    }
}
