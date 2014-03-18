package fb.entity;

public class Order {
	private int oid;
	private String fid;
	private String fullname;
	private String phone;
	private String email;
	private String creditcard;
	
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
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCreditcard() {
		return creditcard;
	}
	public void setCreditcard(String creditcard) {
		this.creditcard = creditcard;
	}
	
}
