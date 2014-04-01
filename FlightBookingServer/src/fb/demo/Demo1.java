package fb.demo;

import fb.dao.UserDao;
import fb.dao.impl.UserDaoImpl;

public class Demo1 {
	public static void main(String[] args) {
		UserDao dao = new UserDaoImpl();
		boolean flag = dao.queryUserExistedFromQan("lily");
		System.out.println(flag);
	}
}
