package BEAN;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import DAO.*;
import com.google.gson.GsonBuilder;

import DAO.CoinHolderDAO;
public class BlockChain {
	private static final int MAX_TXIONS_EACH_PERSON_EACH_EPOCH = 3;
	public static ArrayList<Block> blockchain = new ArrayList<>(); // The blockchain is implemented as an arraylist of Blocks
    public static ArrayList<Transaction> transactions = new ArrayList<>();
    private static ArrayList<CoinHolder> coinHolders= new ArrayList<>();
    public final static int MAX_BLOCKS = 10;
    private static ArrayList<CoinHolder> miners = new ArrayList<>();
    private static final int MINERS_NUM = 4;
    private final static int DIFFICULTY = 4;
    private final static int MINING_REWARDS= 3;
    private static ArrayList<Miner> miner_threads = new ArrayList<>();
    public  static Block createGenesisBlock(){
    	return new Block(0, "\"Everything starts from here!\"", "0");
    }
    
    public static void printChain(){
    	for (int i = 0; i<blockchain.size();i++){
    		System.out.println(blockchain.get(i));
    	}
    }
    public static void loadBlockChain() {
    	blockchain = BlockDAO.getBlockChain();
    }
    public static void loadCoinHolder(){
		ArrayList<CoinHolder> chList =CoinHolderDAO.getAllCoinHolder();
		coinHolders = chList;
    }
	public static int getSender(){
		return coinHolders.get((int)(Math.random() * coinHolders.size())).getId();
	} 
	public  int getRecipient(int senderId) {
		int recipientId ;
		
		do { 
			 recipientId=coinHolders.get((int)(Math.random() * coinHolders.size())).getId();
			}
		while(recipientId == senderId);
		return recipientId;
    }
	public static double getBalance(int id) {
		double balance = coinHolders.get(id-1).getBalance(); // cũ

//        for(int i = 0; i < blockchain.size(); i++){
//            Block currB = blockchain.get(i);
//            for(int j = 0; j < Block.BLOCK_SIZE; j ++){
//                Transaction currT = currB.getTransactions()[j];
//                if (currT == null){
//                    break;
//                }
//                else {
//                    if (currT.getRecipientID() == id) {
//
//                        balance += currT.getAmount();
//
//
//                    } else if (currT.getSenderID() == id) {
//                        balance -= currT.getAmount();
//
//                    }
//                }
//            }
//        }


        return  balance;
	}
	public  void simulate1Transaction() {
		int sender =getSender();
		double senderBalance = getBalance(sender);
		double amount = ((int)(senderBalance * Math.random() * 100))/100.0;
		int recipient =getRecipient(sender);
		transactions.add(new Transaction(sender, recipient, amount));
		
	}
	public  void simulateTransactions() {
        // Tạo ngẫu nhiên 1 số lượng giao dịch cho từng người trong tập coinHolders có thể làm
        if (!coinHolders.isEmpty()) {
            int numTxions = 0;
            for (int i = 0; i < coinHolders.size(); i++) {
                numTxions += (int) (MAX_TXIONS_EACH_PERSON_EACH_EPOCH * Math.random());
            }
            for (int i = 0; i < numTxions; i++) {
                simulate1Transaction();
            }
        }
    }
	
	public static int getMinerAddr() {
		return miners.get((int)(Math.random()*MINERS_NUM)).getId();
	}
	public static boolean numLeading0is(int amount, String hash) {
        boolean result = true;
        int count = 0;
        for (int i = 0; i < hash.length(); i++) {
            if (hash.charAt(i) == '0') {
                count++;
            } else {
                break;
            }
        }
        if (count != amount) {
            result = false;
        }

        return result;
    }
	//Thực hiện vòng lặp tăng nonce cho đến khi nounce thỏa mãn hash của nounce+prevTimestamp có đủ DIFICULTY số 0
//	public static String proofOFwork(long prevTimestamp) {
//        int nounce = Integer.MIN_VALUE;
//        while (!numLeading0is(DIFFICULTY, Encryption.sha256("" + nounce + prevTimestamp))) {
//            nounce++;
//            if (nounce == Integer.MAX_VALUE
//                    && !numLeading0is(DIFFICULTY, Encryption.sha256("" + nounce + prevTimestamp))) {
//                prevTimestamp++;
//                nounce = Integer.MIN_VALUE;
//            }
//        }
//
//        return ("" + nounce + prevTimestamp);
//    }
	public static void retreiveVerifiedTxions(Transaction[] nextToBeConfirmed, int blockId) {
		HashMap<Integer, Double> tempBalanceMap = new HashMap<Integer, Double>();
		int i = 1;
		while (nextToBeConfirmed[nextToBeConfirmed.length - 1] == null
				&& !transactions.isEmpty()) {
			Transaction curr = transactions.get(0);
			
			int sender = curr.getSenderID();
			double balance;
			if (tempBalanceMap.containsKey(sender)) {
				balance = tempBalanceMap.get(sender);
			} else {
				balance = getBalance(sender);
				tempBalanceMap.put(sender, balance);
			}
			if (balance < curr.getAmount() || curr.getAmount() == 0.0) {
				curr.setVerified(false);
			} else {
				curr.setVerified(true);
				nextToBeConfirmed[i] = new Transaction(curr.getIdTran(),curr.getSenderID(),
						curr.getRecipientID(), curr.getAmount(), true,blockId);
				i++;
				balance -= curr.getAmount();
				tempBalanceMap.put(sender, balance);
			}
			transactions.remove(curr);
			
		}
	}

	public  static Block mine() {
		Block lastBlock = blockchain.get(blockchain.size() - 1);
        //String miner_address = getMinerAddr();
        Miner.reset();
        for (int i = 0; i < miners.size(); i++) {
            Miner mt = new Miner(miners.get(i).getId(), lastBlock.getTimestamp(), DIFFICULTY);
            miner_threads.add(mt);
            mt.start();
        }
        for (int i = 0; i < miner_threads.size(); i++) {
            try {
                miner_threads.get(i).join();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted.");
            }
        }
        System.out.println();
        
        miner_threads.removeAll(miner_threads);
        String msg ="";
        for(int i = 0; i < Miner.msg.size(); i++) {
        	msg += Miner.msg.get(i);
        }
        //String nounce;
        //nounce = proofOFwork(lastBlock.getTimestamp());
        Block next =new Block(lastBlock, Miner.final_nonce,DIFFICULTY,  msg);
        Transaction nextToBeConfirmed[] = new Transaction[Block.BLOCK_SIZE];
        int miner_address = Miner.claimerID;
        // rewards to the miner will be the first txion
        nextToBeConfirmed[0] = new Transaction(-1, miner_address, MINING_REWARDS, true,next.getIndex());
        retreiveVerifiedTxions(nextToBeConfirmed, next.getIndex());
        next.setTransactions(nextToBeConfirmed);
        next.setNote("This is Block #" + next.getIndex());
        next.setHash();

        return next;
    }
//	public static boolean addCoinHolder(String recipientId){
//		for (String coinHolder : coinHolders_address) {
//			if(coinHolder.equals(recipientId)){
//				return false;
//			}
//		}
//		coinHolders_address.add(recipientId);
//		return true;
//	}
	public static void updateCoinHolders() {
      
      Block lastB = blockchain.get(blockchain.size()-1);
      for(int i = 0;i < coinHolders.size();i++) {
    	  double balanceChange = 0;
	      for(int j = 0; j < Block.BLOCK_SIZE; j ++){
	          Transaction currT = lastB.getTransactions()[j];
	          if (currT == null){
	              break;
	          }
	          else {
	              if (currT.getRecipientID() == coinHolders.get(i).getId()) {
	            	  balanceChange += currT.getAmount();

	              } else if (currT.getSenderID() == coinHolders.get(i).getId()) {
	            	  balanceChange -= currT.getAmount();

	              }
	          }
	      }
	      coinHolders.get(i).setBalance(coinHolders.get(i).getBalance() + balanceChange);
      }
    }
	public static void loadMiners(){
		ArrayList<CoinHolder> chList =CoinHolderDAO.getAllCoinHolder();
		miners = chList;
		
	}
//    public static void printCoinHolders() {
//        for (int i = 0; i < coinHolders_address.size(); i++) {
//            String addr = coinHolders_address.get(i);
//            System.out.println(addr + " owns: " + getBalance(addr));
//        }
//    }
//    public static void multiMinerMinig(){
//         loadCoinHolder();
//    	 Block newBlock = createGenesisBlock();
//         blockchain.add(newBlock);
//         miners_address = loadMiners();
//
//         for (int i = 0; i < MAX_BLOCKS; i++) {
//             simulateTransactions();
//             newBlock = mine();
//             blockchain.add(newBlock);
//             updateCoinHolders(newBlock);
////           updateTransactions(newBlock);
//         }
//
//         String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
//         System.out.println(blockchainJson);
//         
//         System.out.println("\nAll minted coins: " + MAX_BLOCKS * MINING_REWARDS);
//         double coinsOfHolders = 0;
//         for (int i = 0; i < coinHolders_address.size(); i++) {
//             coinsOfHolders += getBalance(coinHolders_address.get(i));
//         }
//         System.out.println("All coins in coinHolders: " + coinsOfHolders + "\n");
//         printCoinHolders();
//
//
//    	
//   
//
//    	
//    	
//    }
	public static void insertTxions(Block block) {
		Transaction[] txions = block.getTransactions();
		for(int i=0;i < txions.length;i++) {
			if(txions[i] == null) {
				break;
			}else {
				TransactionDAO.insertTransaction(txions[i]);
			}
		}
	}
	public static void updateBalanceDB() {
		for(int i = 0; i < coinHolders.size();i++){
			CoinHolderDAO.updateCoinHolder(coinHolders.get(i).getId(), coinHolders.get(i).getBalance());
        }
	}
}
