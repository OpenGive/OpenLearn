package org.openlearn.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such FileInformation")  // 404
public class FileInformationNotFoundException extends RuntimeException {
	public FileInformationNotFoundException(Long fileInformationId) {
		super("FileInformation with id = '" + fileInformationId.toString() + "' not found.");
	}

}
