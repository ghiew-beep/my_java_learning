package com.example.transactionsystem.exception;

public class UserNoTransactionException extends RuntimeException{
	public UserNoTransactionException(String msg) { super(msg); }
}
