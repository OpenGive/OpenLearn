package org.openlearn.web.rest;

import org.openlearn.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/files")
public class FileResource {

	private static final String ENDPOINT = "/api/files/";

	private static final Logger log = LoggerFactory.getLogger(CourseResource.class);

	private final FileRepository fileRepository;

	public FileResource(final FileRepository fileRepository) {
		this.fileRepository = fileRepository;
	}
}
