package mutsa.sns.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // USER
    DUPLICATED_USER(HttpStatus.CONFLICT, "이미 사용중인 ID 입니다."),
    PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED,"비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, " 사용자를 찾을 수 없습니다."),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "사용자 권한이 없습니다."),

    // Article
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, " 게시글을 찾을 수 없습니다."),

    // Image,
    IMAGE_NOT_FOUND(HttpStatus.BAD_REQUEST,"이미지를 첨부해주세요"),
    DIRECTORY_EXISTS(HttpStatus.INTERNAL_SERVER_ERROR,"이미 해당 이름의 폴더가 존재합니다."),

    // Like
    LIKES_NOT_FOUND(HttpStatus.NOT_FOUND,"좋아요가 존재하지 않습니다."),
    LIKES_ALREADY_EXISTS(HttpStatus.CONFLICT,"좋아요가 이미 존재합니다."),

    // Follow
    FOLLOW_NOT_FOUND(HttpStatus.NOT_FOUND,"팔로우 정보를 찾을 수 없습니다."),

    // Friend
    FRIEND_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND,"친구 신청 정보를 찾을 수 없습니다."),

    // COMMENT
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"댓글을 찾을 수 없습니다." );

    private HttpStatus status;
    String msg;
    ErrorCode( HttpStatus status, String msg) {
        this.status = status;
        this.msg = msg;
    }

}
