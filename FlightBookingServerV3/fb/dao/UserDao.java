package fb.dao;

import fb.entity.User;

public interface UserDao {
	public boolean queryUserExistedFromCea(String username);
	
	public boolean queryUserExistedFromAc(String username);
	
	public boolean queryUserExistedFromQan(String username);
	
	public boolean insertUserToQan(User user);
	public boolean insertUserToAc(User user);
	public boolean insertUserToCea(User user);
}
