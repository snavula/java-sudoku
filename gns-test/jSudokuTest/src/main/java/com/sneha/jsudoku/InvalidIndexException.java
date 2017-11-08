package com.sneha.jsudoku;

public class InvalidIndexException extends Exception {
	
	private static final long serialVersionUID = -8631354917953283096L;

	public InvalidIndexException(String s) {
		super(s);
	}
	
	public InvalidIndexException(String s, Exception ex) {
		super(s, ex);
	}
	

}
