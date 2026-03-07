public class Program {
	public static void main(String[] args) {
		User p1 = new User("John", 1000);
		User p2 = new User("Kate", 100);
		UserIdsGenerator gen;

		System.out.println(p1.getName() + "'s balance is: " + p1.getBalance());
		System.out.println(p2.getName() + "'s balance is: " + p2.getBalance());

		System.out.println(p1.getIdentifier());
		System.out.println(p2.getIdentifier());
	}
}