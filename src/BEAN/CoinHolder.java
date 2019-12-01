package BEAN;

public class CoinHolder {
	private int id;
	private String username;
	private double balance;
	public CoinHolder(int id, String username, float balance) {
		super();
		this.id = id;
		this.username = username;
		this.balance = balance;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
}
