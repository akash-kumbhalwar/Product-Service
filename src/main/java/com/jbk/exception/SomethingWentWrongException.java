package com.jbk.exception;

import net.bytebuddy.implementation.bind.annotation.Super;

public class SomethingWentWrongException extends RuntimeException {

	public SomethingWentWrongException(String msg) {
		
		super(msg);
		
	}

}
