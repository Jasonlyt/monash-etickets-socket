package fb.common;


public interface ProxyServerSevice {
	
	public String[] queryResp(String input);
	
	public boolean regResp(String str);
		
	public boolean orderResp(String str);
	
	public String[] checkOrders(String str);
	
	public void quit();
}
