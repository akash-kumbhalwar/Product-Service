package com.jbk.exception;

public class ResourceNotExistException extends RuntimeException{
	
	private static final long serialVersionUID = -8005779469704090983L;

	public ResourceNotExistException (String msg) {
		
		super(msg);
	}

}
