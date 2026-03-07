public class User {
	private Integer identifier;
	private String name;
	private Integer balance;
	private Transaction[] transactionHistory;
	private int transactionCount;

	private static final int MAX_RECORD = 1000;

	public User () {
		this.identifier = UserIdsGenerator.getInstance().generateId();
		this.name = "Alpha";
		this.balance = 0;
		this.transactionHistory = new Transaction[MAX_RECORD];
		this.transactionCount = 0;
	}

	public User (String name, Integer balance) {
		this.identifier = UserIdsGenerator.getInstance().generateId();
		this.name = name;
		this.balance = balance;
		this.transactionHistory = new Transaction[MAX_RECORD];
		this.transactionCount = 0;
	}

	public Integer getIdentifier() { return identifier; }
	public String getName() { return name; }
	public Integer getBalance() { return balance; }
	public void setBalance(Integer offset) { balance = balance + offset; }

	public Transaction getTransactionHistory(int idx) {
		if (idx < 0 || idx >= transactionCount || transactionHistory[idx] == null) {
			System.out.println("Invalid transaction index detected");
			return null;
		}
		return transactionHistory[idx];
	}
}