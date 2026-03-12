package com.example.transactionsystem.exception;

public class TransactionNotFoundException extends RuntimeException {
	public TransactionNotFoundException(String msg) { super(msg); }
}