package org.openlearn.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.FORBIDDEN, reason="Request Action is Forbidden")  // 403
public class AccessDeniedException extends RuntimeException {
}
