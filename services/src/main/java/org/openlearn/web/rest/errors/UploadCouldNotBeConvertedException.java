package org.openlearn.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNPROCESSABLE_ENTITY, reason="Upload file is invalid")
public class UploadCouldNotBeConvertedException extends RuntimeException {
	public UploadCouldNotBeConvertedException() { super(); }
}
