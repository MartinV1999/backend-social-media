package com.backend.backend.services;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3ServiceImpl implements IS3Service {

  String bucketName = "aws-social-media-gokias";

  private final S3Client s3Client;

  public S3ServiceImpl(S3Client s3Client){
    this.s3Client = s3Client;
  }

  public String uploadFile(String filename,UUID uuid ,String ruta, MultipartFile file) throws IOException {
    try {
      String path = ruta + uuid + "/" + filename;
      PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(path)
        .contentLength(file.getSize())
        .contentType(file.getContentType())
        .build();

      s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

      // Obtener la URL del archivo recién cargado
      String fileUrl = s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(path)).toExternalForm();

      return fileUrl;
    } catch (Exception e) {
      throw new IOException(e.getMessage());
    }
  }

  @Override
  public Boolean deleteFile(String ruta, String filename, UUID uuid) throws IOException {
    try {
      String path = ruta + uuid + "/" + filename;
      DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest
        .builder()
        .bucket(bucketName)
        .key(path)
        .build();
      
      s3Client.deleteObject(deleteObjectRequest);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

}
