package com.shopme;

import java.nio.file.Paths;
import java.util.List;
import java.nio.file.Path;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
	
	private static final List<String> PATHS = List.of("user-photos", "../category-images", "../brands-logos",
														"../product-images");
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		for (String path: MvcConfig.PATHS) {
			exposeDirectory(path, registry);
		}
	}
	
	public void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry) {
		Path photoDirection = Paths.get(pathPattern);
		String absPath = photoDirection.toFile().getAbsolutePath();
		
		String logicalPath = pathPattern.replace("../", "") + "/**";
		
		registry.addResourceHandler(logicalPath).addResourceLocations("file:/" + absPath + "/");
	}
}
