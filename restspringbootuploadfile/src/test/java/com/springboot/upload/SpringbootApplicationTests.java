package com.springboot.upload;

import java.nio.file.Paths;
import java.util.stream.Stream;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.springboot.exception.UploadFileException;
import com.springboot.uploadcontroller.SpringbootApplication;
import com.springboot.uploadservice.FileUploadService;

/* This is spring boot test class which test all the methods that have been defined in service layer.
 * 
 */

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = { SpringbootApplication.class })
public class SpringbootApplicationTests {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private FileUploadService fileUploadService;

	@Test
	public void shouldListAllFiles() throws Exception {
		given(this.fileUploadService.loadAll()).willReturn(Stream.of(Paths.get("first.txt"), Paths.get("second.txt")));

		this.mvc.perform(get("/")).andExpect(status().isOk()).andExpect(model().attribute("files",
				Matchers.contains("http://localhost/files/first.txt", "http://localhost/files/second.txt")));
	}

	@Test
	public void shouldSaveUploadedFile() throws Exception {
		MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain",
				"Spring Framework".getBytes());
		this.mvc.perform(fileUpload("/").file(multipartFile)).andExpect(status().isFound())
				.andExpect(header().string("Location", "/"));

		then(this.fileUploadService).should().store(multipartFile);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void should404WhenMissingFile() throws Exception {
		given(this.fileUploadService.loadAsResource("test.txt")).willThrow(UploadFileException.class);

		this.mvc.perform(get("/files/test.txt")).andExpect(status().isNotFound());
	}

}