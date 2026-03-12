import java.util.UUID;

public class User {
	private Integer identifier;
	private String name;
	private Integer balance;
	private TransactionList lst = new TransactionsLinkedList();
	private int transactionCount;

	private User () {}

	public User (String name, Integer balance) {
		this.identifier = UserIdsGenerator.getInstance().generateId();
		this.name = name;
		this.balance = balance;
		this.transactionCount = 0;
	}

	public Integer getIdentifier() { return identifier; }
	public String getName() { return name; }
	public Integer getBalance() { return balance; }
	public void	addTransaction(Transaction item) {
		lst.add(item);
		transactionCount++;
	}

//	public void removeTransaction(UUID transactionID) {
//		Transaction[] record = lst.toArray();
//		for (int i = 0; i < record.length; i++) {
//			if (record[i].getUUID().equals(transactionID)) {
//				lst.remove(transactionID);
//			}
//		}
//	}

	public void removeTransaction(UUID transactionID) {
		lst.remove(transactionID);
	}
}