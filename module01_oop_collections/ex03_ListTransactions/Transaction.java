import java.util.UUID;

public class Transaction {
	private final UUID identifier;
	private final User recipient;
	private final User sender;
	private final TransferCategory transfer_category;
	private final Integer transfer_amount;

	public Transaction(User sender, User recipient, TransferCategory value, Integer transfer_amount) {

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