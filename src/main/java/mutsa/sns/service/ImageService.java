package mutsa.sns.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import mutsa.sns.domain.entity.ArticleEntity;
import mutsa.sns.domain.entity.ArticleImageEntity;
import mutsa.sns.exception.CustomException;
import mutsa.sns.exception.ErrorCode;
import mutsa.sns.repository.ArticleImageRepository;
import mutsa.sns.repository.ArticleRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {
    private final ArticleImageRepository articleImageRepository;
    private final ArticleRepository articleRepository;


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

        ArticleEntity articleEntity = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        ArticleImageEntity articleImageEntity = new ArticleImageEntity();
        articleImageEntity.setArticle(articleEntity);

        // 이미지가 없을 때 default 이미지 저장.
        if(image == null){
            log.info("defaultArticleImgUrl = {}",String.format("%s/default.png",folderName));
            Path defaultArticleImgUrl = Path.of(String.format("%s/default.png",folderName));
//            image.transferTo(defaultArticleImgUrl);
            articleImageEntity.setImageUrl(defaultArticleImgUrl.toString());
            articleImageRepository.save(articleImageEntity);
            return defaultArticleImgUrl;
        }
        // 현재 시간을 파일이름 저장
        LocalDateTime now = LocalDateTime.now();
        log.info("articleImgUrl = {}",String.format("%s/%s.png",folderName, now.toString().replace(":", "")));
        Path articleImgUrl = Path.of(String.format("%s/%s.png",folderName, now.toString().replace(":", "")));
        log.info("게시글 이미지 저장 완료");
        image.transferTo(articleImgUrl);
        articleImageEntity.setImageUrl(articleImgUrl.toString());
        articleImageRepository.save(articleImageEntity);

        return articleImgUrl;

    }

    public void deleteImage(String username,Integer articleId) throws IOException {

        String folderName = String.format("images/%s/article_%s", username, articleId);
        FileUtils.deleteDirectory(new File(folderName));
        ArticleEntity articleEntity = articleRepository.findById(articleId).get();
        List<ArticleImageEntity> articleImageUrlList = articleEntity.getArticleImageUrlList();
        for (ArticleImageEntity articleImageEntity : articleImageUrlList) {
            articleImageRepository.delete(articleImageEntity);
        }


    }

}
