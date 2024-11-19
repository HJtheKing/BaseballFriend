package com.teamjhw.bestfriend.global.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3Util {

    @Value("${s3.bucket-name}")
    private String bucketName;

    private final AmazonS3 amazonS3;

    /**
     * S3 Upload 요청 메서드
     */
    public String uploadFile(MultipartFile file) {
        if (file.isEmpty() || Objects.isNull(file.getOriginalFilename())) {
            throw new AmazonS3Exception("파일이 비어있습니다");
        }
        return this.uploadFileToS3(file);
    }

    /**
     * S3 실제 업로드
     */
    private String uploadFileToS3(MultipartFile file) {
        String s3FileName = UUID.randomUUID().toString().substring(0, 10); // 변경된 파일 명

        try (InputStream is = file.getInputStream();
             ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(IOUtils.toByteArray(is))) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/jpg");
            metadata.setContentLength(byteArrayInputStream.available());
            // S3 업로드
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(putObjectRequest);
            return amazonS3.getUrl(bucketName, s3FileName).toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AmazonS3Exception("S3 업로드에 실패했습니다.");
        }
    }

}
