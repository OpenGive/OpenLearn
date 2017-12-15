package org.openlearn.web.rest.errors;

import org.openlearn.domain.FileInformation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="S3 File Access Failed")
public class FileInformationAccessFailedException extends RuntimeException {
	public FileInformationAccessFailedException() { super(); }

	public FileInformationAccessFailedException(String message) {
		super(message);
	}
}
