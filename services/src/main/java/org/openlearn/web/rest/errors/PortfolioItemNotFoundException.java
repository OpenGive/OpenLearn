package org.openlearn.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such PortfolioItem")  // 404
public class PortfolioItemNotFoundException extends RuntimeException {
	public PortfolioItemNotFoundException(Long portfolioItemId) {
		super("PortfolioItem with id = '" + portfolioItemId.toString() + "' not found.");
	}
}
