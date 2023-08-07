package mutsa.sns.service;

import lombok.extern.slf4j.Slf4j;
import mutsa.sns.exception.CustomException;
import mutsa.sns.exception.ErrorCode;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

@Slf4j
@Service
public class ImageService {

    // 폴더 생성
    private static String makeFolder(String name){
        String path = String.format("images/%s/", name);

        if(!Files.exists(Path.of(path))){
            //파일 경로 위치 예외처리
            try{
                Files.createDirectories(Path.of(path));
            }
            catch (IOException e){
                throw new CustomException(ErrorCode.DIRECTORY_EXISTS);
            }
        }
        return path;
    }


    public Path uploadProfileImage(String username ,MultipartFile image) throws IOException {

        String name = String.format("/%s/profile", username);
        String folderName = makeFolder(name);


        // 현재 시간을 파일이름 저장
        LocalDateTime now = LocalDateTime.now();
        log.info("profileImgUrl = {}",String.format("%s/%s.png",folderName, now.toString().replace(":", "")));
        Path profileImgUrl = Path.of(String.format("%s/%s.png",folderName, now.toString().replace(":", "")));
        log.info("프로필 이미지 저장 완료");
        image.transferTo(profileImgUrl);
        return profileImgUrl;

    }

    public Path uploadArticleImage(String username ,Integer articleId , MultipartFile image) throws IOException {

        String name = String.format("/%s/article_%s", username, articleId);
        String folderName = makeFolder(name);

        // 현재 시간을 파일이름 저장
        LocalDateTime now = LocalDateTime.now();
        log.info("profileImgUrl = {}",String.format("%s/%s.png",folderName, now.toString().replace(":", "")));
        Path profileImgUrl = Path.of(String.format("%s/%s.png",folderName, now.toString().replace(":", "")));
        log.info("게시글 이미지 저장 완료");
        image.transferTo(profileImgUrl);
        return profileImgUrl;

    }

    public void deleteImage(String username) throws IOException {
        FileUtils.deleteDirectory(new File(String.format("%s/",username)));
    }

}
