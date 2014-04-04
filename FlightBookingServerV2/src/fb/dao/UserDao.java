package fb.dao;

import fb.entity.User;

public interface UserDao {
	public boolean queryUserExistedFromCEA(String username);
	
	public boolean queryUserExistedFromAC(String username);
	
	public boolean queryUserExistedFromQan(String username);
	
	public boolean insertUserToQan(User user);
	public boolean insertUserToAc(User user);
	public boolean insertUserToCea(User user);
}
