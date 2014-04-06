package fb.common;

public interface ProxyClientService {
	public String[] queryReq(String input);
	public String[] checkReq(String input);
	public boolean regReq(String input);
	public boolean orderReq(String fid);
	public void quit();
}
