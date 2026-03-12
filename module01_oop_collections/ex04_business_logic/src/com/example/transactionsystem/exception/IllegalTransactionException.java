package com.example.transactionsystem.exception;

public class IllegalTransactionException extends RuntimeException {
	public IllegalTransactionException(String msg) { super(msg); }
}