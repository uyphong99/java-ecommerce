package com.shopme.admin;

import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.nio.file.Path;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
	
	private static final List<String> PATHS = List.of("user-photos", "../category-images", "../brands-logos",
														"../product-images", "../site-logo");
	
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

	@Bean
	public ConversionService conversionService() {
		DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
		DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
		registrar.setDateFormatter(DateTimeFormatter.ISO_DATE);
		registrar.setDateTimeFormatter(DateTimeFormatter.ISO_DATE_TIME);
		registrar.registerFormatters(conversionService);
		return conversionService;
	}
}
