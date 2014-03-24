package fb.entity;

public class Order {
	private int oid;
	private String fid;
	private String username;

	
	public String getFid() {
		return fid;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public int getOid() {
		return oid;
	}
	public void setOid(int oid) {
		this.oid = oid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String fullname) {
		this.username = fullname;
	}
}
