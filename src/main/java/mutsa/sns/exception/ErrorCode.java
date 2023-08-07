package mutsa.sns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    DUPLICATED_USER(HttpStatus.CONFLICT, "이미 사용중인 ID 입니다."),
    PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED,"비밀번호가 일치하지 않습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, " 를(을) 찾을 수 없습니다."),
    IMAGE_NOT_FOUND(HttpStatus.BAD_REQUEST,"이미지를 첨부해주세요"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "사용자 권한이 없습니다."),
    DIRECTORY_EXISTS(HttpStatus.INTERNAL_SERVER_ERROR,"이미 해당 이름의 폴더가 존재합니다.");

    private HttpStatus status;
    String msg;
    ErrorCode( HttpStatus status, String msg) {
        this.status = status;
        this.msg = msg;
    }

}
