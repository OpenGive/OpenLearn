package org.openlearn.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Students under the age of 14 cannot have an email")  // 400
public class StudentEmailForUnderFourteenException extends RuntimeException {
	public StudentEmailForUnderFourteenException(String msg) {
		super(msg);
	}
}
