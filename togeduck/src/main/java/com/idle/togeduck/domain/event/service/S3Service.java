package com.idle.togeduck.domain.event.service;

import static com.idle.togeduck.global.response.ErrorCode.*;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.idle.togeduck.global.response.BaseException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class S3Service {

	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	public String saveFile(MultipartFile multipartFile) {
		UUID uuid = UUID.randomUUID();
		StringBuilder fileName = new StringBuilder(uuid.toString());
		fileName.append(multipartFile.getOriginalFilename());

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(multipartFile.getSize());
		metadata.setContentType(multipartFile.getContentType());

		try {
			amazonS3.putObject(bucket, fileName.toString(), multipartFile.getInputStream(), metadata);
		} catch (IOException e) {
			throw new BaseException(DATA_CANT_SAVE);
		}
		return amazonS3.getUrl(bucket, fileName.toString()).toString();
	}

	public void deleteFile(String imagePath) {
		String splitStr = ".com/";
		String fileName = imagePath.substring(imagePath.lastIndexOf(splitStr) + splitStr.length());

		amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
	}
}
