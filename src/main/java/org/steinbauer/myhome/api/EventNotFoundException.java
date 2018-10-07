package org.steinbauer.myhome.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.NOT_FOUND)
public class EventNotFoundException extends Exception {

	private static final long serialVersionUID = -4581015166930697431L;
	
	public EventNotFoundException(String id) {
		super(String.format("No event found with UUID: '%s'", id));
	}

	public EventNotFoundException(int index) {
		super(String.format("No event found for index: %d", index));
	}

}
