package edu.school21.chat.exception;

public class NotSavedSubEntityException extends RuntimeException {
	public NotSavedSubEntityException(String message) {
		super(message);
	}

	public NotSavedSubEntityException(String message, Throwable cause) {
		super(message, cause);
	}
}
