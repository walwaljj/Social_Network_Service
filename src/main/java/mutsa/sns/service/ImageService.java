package mutsa.sns.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;

@Slf4j
@Service
public class ImageService {

    // 폴더 생성
    private String makeFolder(String username){
        // Path userImgDirectory = Files.createDirectory(Path.of(folderName));
        String folderName = String.format("%s/", username);
        File folder = new File(folderName);
        if(!folder.exists()) {
            folder.mkdirs();
            log.info("crateFolder name = {} ", folder.getName());
            return folder.getName();
        }
        return folderName;
    }

    public Path uploadImage(String username ,MultipartFile image) throws IOException {

        String folderName = makeFolder(username);

        // 현재 시간을 파일이름 저장
        LocalDateTime now = LocalDateTime.now();
        Path profileImgUrl = Path.of(String.format("%s/%s.png", folderName, now.toString().replace(":", "")));
        image.transferTo(profileImgUrl);
        return profileImgUrl;

    }

    public void deleteImage(String username) throws IOException {
        FileUtils.deleteDirectory(new File(String.format("%s/",username)));
    }

}
