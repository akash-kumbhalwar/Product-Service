package com.jbk.exception;

public class ResourceAlreadyExistException extends RuntimeException {
	
	private static final long serialVersionUID = 5863514511128074299L;

	public ResourceAlreadyExistException(String msg) {
		super(msg);
	}

}