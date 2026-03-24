package com.example.transactionsystem.repository;

import com.example.transactionsystem.exception.TransactionNotFoundException;
import com.example.transactionsystem.model.Transaction;
import java.util.UUID;

public class TransactionsLinkedList implements TransactionsList {
	private int ownerID = 0;
	private Node head = null;
	private int nodeCount = 0;

	public TransactionsLinkedList(int ownerID) {
		this.ownerID = ownerID;
	}

	private TransactionsLinkedList() {}

	//static here means this nested class holds no ref to the outer class
	//meaning it cannot call or access the data member or method of the outer class
	static class Node {
		Transaction item;
		Node prev;
		Node next;

		Node(Transaction item) {
			this.item = item;
			this.prev = null;
			this.next = null;
		}
	}

	@Override
	public void add(Transaction item) {
		Node newHead = new Node(item);
		if (head != null) {
			newHead.next = head;
			head.prev = newHead;
		}
		head = newHead;
		nodeCount++;
	}

	@Override
	public void remove(String UUID_identifier)
			throws TransactionNotFoundException {
		if (nodeCount == 0) {
			throw new TransactionNotFoundException("User: " + ownerID + "\nhas no transaction record");
		}
		UUID transactionID = UUID.fromString(UUID_identifier);
		Node current = head;
		while (current != null) {
			if (current.item.getTransactionID().equals(transactionID)) {
				if (nodeCount == 1) {//removing single node linkedlist
					head = null;
				}
				else if (current.prev == null && current.next != null) {//removing head node
					current.next.prev = null;
					head = current.next;
				}
				else if (current.prev != null && current.next != null) {//removing node in the middle
					current.prev.next = current.next;
					current.next.prev = current.prev;
				}
				else if (current.prev != null){//removing last node
					current.prev.next = null;
				}
				--nodeCount;
				break ;
			}
			current = current.next;
		}
		throw new TransactionNotFoundException("No relevant transaction found for user " + ownerID);
	}

	@Override
	public Transaction[] toArray() {
		if (nodeCount == 0) {
			return null;
		}
		Transaction[] list = new Transaction[nodeCount];
		Node cur = head;
		for (int i = 0; i < nodeCount; i++) {
			list[i] = cur.item;
			cur = cur.next;
		}
		return list;
	}

	public boolean isEmpty() {
		return nodeCount == 0;
	}
}