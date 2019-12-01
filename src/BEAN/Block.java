package BEAN;
import java.util.Date;
public class Block {
	private int index;
	private long timestamp;
	private String note;
	private Transaction transactions[];
	private String prev_hash;
	private String hash;
	private int difficulty;
	private String nonce;
	private String msg;
	
	public static final int DIFFICULTY = 4;
	public static final int BLOCK_SIZE = 5;
	
	public Block(int index, String note, String prev_hash) {
		super();
		this.index = index;
		this.note = note;
		this.prev_hash = prev_hash;
		this.transactions = new Transaction[BLOCK_SIZE];
	}

	public Block(int index, String data, String prev_hash, String nonce, int difficulty){
		this.index = index;
		this.timestamp = new Date().getTime();
		this.prev_hash = prev_hash;
		this.hash = calcHash();
		this.nonce =nonce;
		this.transactions = new Transaction[BLOCK_SIZE];
	}
	

	public Block(Block prevBlock, String nonce,int difficulty, String msg) {
		this.index = prevBlock.getIndex() +1;
		this.prev_hash = prevBlock.hash;
		this.transactions = new Transaction[BLOCK_SIZE];
		this.nonce = nonce;
		this.hash = calcHash();
		this.note = "This is Block #" + prevBlock.getIndex();
		this.timestamp = new Date().getTime();
		this.difficulty = difficulty;
		this.msg = msg;
	}

	public Block(int id, String note2, String prev_hash, String cur_hash, String nonce) {
		this.index = id;
		this.note = note;
		this.prev_hash = prev_hash;
		this.hash = cur_hash;
		this.nonce = nonce;
		this.transactions = new Transaction[BLOCK_SIZE];
	}

	public Block() {
		// TODO Auto-generated constructor stub
	}

	public int getIndex() {
		return index;
	}

	public String getPrev_hash() {
		return prev_hash;
	}

	public String getHash() {
		return hash;
	}

	public long getTimestamp() {
		return this.timestamp;
	}
	public Transaction[] getTransactions(){
		return this.transactions;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	
	public String getNote() {
		return note;
	}
	

	public int getDifficulty() {
		return difficulty;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	
	public void setTransactions(Transaction[] txions){
		for (int i = 0; i < this.transactions.length; i++) {
            this.transactions[i] = txions[i];
        }
	}
	public String getMsg() {
		return this.msg;
	}
	private String calcHash(){
		return Encryption.sha256(this.index + this.timestamp + this.note + this.transactions + this.prev_hash);
	}

	public String  toString() {
		return ("Block #" + this.index + "\n\tmined at:" + this.timestamp +"\n\t Note:" + this.note + "\n\ntHash:"
				+ "{"
				+ this.hash 
				+ "}\n");
	}

	public void setHash() {
		this.hash = calcHash();
		
	}


	
	
}
