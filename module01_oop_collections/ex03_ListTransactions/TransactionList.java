import java.util.UUID;

interface TransactionList {
	void add(Transaction item);
	void remove(UUID transactionID);
	Transaction[] toArray();
}