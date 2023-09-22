package com.backend.backend.services;

import java.io.IOException;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public interface IS3Service {
  String uploadFile(String filename, Integer counter,UUID id, String ruta,MultipartFile file) throws IOException;
  Boolean deleteFile(String ruta,String filename ,UUID id) throws IOException;

}
