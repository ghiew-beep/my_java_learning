import java.util.UUID;

public class Transaction {
	private UUID identifier;
	private User recipient;
	private User sender;
	private TransferCategory transfer_category;
	private Integer transfer_amount;

	public Transaction(User recipient, User sender, TransferCategory value, Integer transfer_amount) {

		if (value == TransferCategory.CREDITS && transfer_amount < 0) {
			System.out.println("Credits cannot be negative");
		}

		if (value == TransferCategory.DEBITS && transfer_amount > 0) {
			System.out.println("Debits cannot be positive");
		}

		this.identifier = UUID.randomUUID();
		this.recipient = recipient;
		this.sender = sender;
		this.transfer_category = value;
		this.transfer_amount = transfer_amount;
	}

	public UUID getUUID() { return identifier; }
	public User getRecipient() { return recipient; }
	public User getSender() { return sender; }
	public TransferCategory getTransferCategory() { return transfer_category; }
	public Integer getTransferAmount() { return transfer_amount; }
}