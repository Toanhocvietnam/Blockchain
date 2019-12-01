package BEAN;
import java.util.ArrayList;

public class Transaction {
	private static int ID = 0;
	private int idTran;
	private int blockId;
	private int senderID;
	private int recipientID;
	private double amount;
	private boolean verified;
	
	public Transaction(int sID, int rID, double amt){
		this.idTran= ID;
		ID++;
		this.senderID = sID;
		this.recipientID = rID;
		this.amount = amt;
		this.verified = false;
		
	}
	public Transaction(int sID, int rID, double amt,boolean verified, int blockId){
		this.idTran= ID;
		ID++;
		this.senderID = sID;
		this.recipientID = rID;
		this.amount = amt;
		this.verified = verified;
		this.blockId = blockId;
	}
	public Transaction(int idTran,int sID, int rID, double amt, boolean verified,int blockId){
		this.idTran = idTran;
		this.senderID = sID;
		this.recipientID = rID;
		this.amount = amt;
		this.verified = verified;
		this.blockId = blockId;
	}
	
	

	public int getIdTran() {
		return idTran;
	}
	public void setIdTran(int idTran) {
		this.idTran = idTran;
	}
	
	public int getBlockId() {
		return blockId;
	}
	public void setBlockId(int blockId) {
		this.blockId = blockId;
	}
	public int getSenderID() {
		return senderID;
	}

	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}

	public int getRecipientID() {
		return recipientID;
	}

	public void setRecipientID(int recipientID) {
		this.recipientID = recipientID;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	
	
}
