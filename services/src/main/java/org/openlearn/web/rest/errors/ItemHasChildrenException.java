package org.openlearn.web.rest.errors;

import org.springframework.util.StringUtils;

public class ItemHasChildrenException extends RuntimeException {

	public ItemHasChildrenException(String message) {
		super(message);
	}
}
