interface UsersList {
	void addUser(User input);
	User getUser(Integer id);
	User getUser(int idx);
	int getUserCount();
}