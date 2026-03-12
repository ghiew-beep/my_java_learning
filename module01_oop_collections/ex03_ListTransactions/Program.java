import java.util.UUID;

public class Program {
	public static void main(String[] args) {
		User p1 = new User("John", 1000);
		User p2 = new User("Kate", 100);
		UserIdsGenerator gen;

		System.out.println(p1.getName() + "'s balance is: " + p1.getBalance());
		System.out.println(p2.getName() + "'s balance is: " + p2.getBalance());

		System.out.println("p1 identifier: " + p1.getIdentifier());
		System.out.println("p1 identifier: " + p2.getIdentifier());

		UsersList lst = UsersArrayList.getInstance();
		lst.addUser(p1);
		lst.addUser(p2);

		System.out.println("Total user count: " + lst.getUserCount());

		//just testing
		p1.addTransaction(new Transaction(p1, p2, TransferCategory.DEBITS, -100));
		p2.addTransaction(new Transaction(p1, p2, TransferCategory.CREDITS, 100));

		//demo unchecked exception that will halt the program
		UUID testing = UUID.randomUUID();
		p1.removeTransaction(testing);
	}
}