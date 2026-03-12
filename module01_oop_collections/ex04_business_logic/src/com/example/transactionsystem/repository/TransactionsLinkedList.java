package com.example.transactionsystem.repository;

import com.example.transactionsystem.exception.TransactionNotFoundException;
import com.example.transactionsystem.model.Transaction;

import java.util.UUID;

public class TransactionsLinkedList implements  TransactionList {
	private Node head;
	private int nodeCount;

	private static class Node {
		private final Transaction item;
		private Node prev;
		private Node next;

		Node(Transaction data) {
			this.item = data;
		}
	}

	public TransactionsLinkedList() {}

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
	public void remove(UUID transactionID) {
		Node ptr = head;

		while (ptr != null) {
			if (ptr.item.getUUID().equals(transactionID)) {

				Node left = ptr.prev;
				Node right = ptr.next;

				if (left == null && right != null) {
					head = right;
					right.prev = null;
				} else if (left != null && right == null) {
					left.next = null;
				} else if (left != null && right != null) {//Condition 'right != null' is always 'true' when reached
					left.next = right;
					right.prev = left;
				} else {
					head = null;
				}

				nodeCount--;

				return ;
			}
			ptr = ptr.next;
		}
		throw new TransactionNotFoundException("No such record: " + transactionID);
	}

	@Override
	public Transaction[] toArray() {
		Transaction[] lst = new Transaction[nodeCount];

		Node ptr = head;
		int i = 0;

		while (ptr != null) {
			lst[i] = ptr.item;
			ptr = ptr.next;
			i++;
		}
		return (lst);
	}
}