package com.springboot.uploadcontroller;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.springboot.uploadservice.FileUploadService;
import com.springboot.uploadservice.FileUploadServiceImpl;
import com.springboot.uploadservice.StorageProperties;


@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class) 
public class SpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootApplication.class, args);
	}
	
	//Each time when application starts, delete the existing files and initialize it
	 @Bean
	    CommandLineRunner init(FileUploadService fileUploadService) {
	        return (args) -> {
	        	fileUploadService.deleteAll();
	        	fileUploadService.init();
	        };
	    }
	 
	 //Service bean for all the operations
	 @Bean
	 public FileUploadServiceImpl fileUploadServiceImpl(StorageProperties storageProperties)
	 {
		 return new FileUploadServiceImpl(storageProperties);
	 }
	 
	
}
