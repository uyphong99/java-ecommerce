package com.shopme.admin;

import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
/**
 * The class handle the uploaded multipart file (image) given the directory as the parameter
 * */
public class FileUploadUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);
	
	/**
	 * Create directory, if dir not already exists, make one and store multipart file to dir
	 * */
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
	
	/**
	 * Clear all files in directory except directory.
	 * */
	public static void cleanDir(String dir) {
		Path dirPaths = Paths.get(dir);
		
		try {
			Files.list(dirPaths).forEach(file -> {
				if (!Files.isDirectory(file)) {
					try {
						Files.delete(file);
					} catch (IOException exception) {
						LOGGER.error("Could not delete file: " + file);
					}
				}
			});
		} catch (IOException exception) {
			LOGGER.error("Could not list directory: " + dirPaths);
		}
	}
	
	public static void removeDir(String dir) {
		cleanDir(dir);
		
		try {
			Files.delete(Paths.get(dir));
		} catch (IOException e) {
			LOGGER.error("Could not remove directory: " + dir);
		}
		
	}
}
