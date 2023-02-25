package com.shopme.admin;

import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
	public static void saveFile(String uploadDir, String fileName,
			MultipartFile multipartFile) throws IOException {
		Path uploadPath = Paths.get(uploadDir);
		
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		
		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ex){
			throw new IOException("Could not save file: " + fileName, ex);
		}
	}
	
	public static void cleanDir(String dir) {
		Path dirPaths = Paths.get(dir);
		
		try {
			Files.list(dirPaths).forEach(file -> {
				if (!Files.isDirectory(file)) {
					try {
						Files.delete(file);
					} catch (IOException exception) {
						System.out.println("Could not delete file: " + file);
					}
				}
			});
		} catch (IOException exception) {
			System.out.println("Could not list directory: " + dirPaths);
		}
	}
}
