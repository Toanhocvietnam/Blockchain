package BEAN;
import java.util.ArrayList;


public class Miner extends Thread {
	public static boolean solutionClaimed = false;// cho các miner biết rằng block mới vừa được đào, và ngừng việc đào 
    public static int claimerID = -1;//ID của người thông báo giải dc POW
    public static int candidate = Integer.MIN_VALUE; // số nonce vừa được đào
    public static ArrayList<Boolean> consensusList = new ArrayList<Boolean>();//Sự đồng thuận của các miner vs POW của người thông báo đã tìm dc nonce
    public static int minerNum = 1; //Số lượng thợ đào
    public static String final_nonce;
    public static ArrayList<String> msg;
    private int difficulty;
    
    
    
    private long prevInfo;

    public int index; // id của thread
    public String solution;

    public Miner(int minerID, long prevInfo, int difficulty) {
        super(String.valueOf(minerID));
        index = minerID;
        minerNum++;
        consensusList.add(false);
        solution = "";
        this.prevInfo = prevInfo;
        this.difficulty = difficulty;

        //System.out.println("Creating miner thread: " + minerID);
    }
    private boolean consensusAchieved() {
        boolean agree = true;
        for (int i = 0; i < consensusList.size(); i++) {
            if (consensusList.get(i) == false) {
                agree = false;
                break;
            }
        }
        return agree;
    }
    public static void reset() {
        solutionClaimed = false;
        claimerID = -1;
        candidate = Integer.MIN_VALUE;
        for (int i = 0; i < consensusList.size(); i++) {
            consensusList.removeAll(consensusList);
        }
        minerNum = 0;
        final_nonce = "";
        msg = new ArrayList<String>();
    }
    private void resetConsensus() {
        // 1, reset the consensusList to all false
        for (int i = 0; i < consensusList.size(); i++) {
            consensusList.set(i, false);
        }
        // 2, reset candidate
        candidate = Integer.MIN_VALUE;
        // 3, reset solutionClaimed to false
        solutionClaimed = false;
        // 4, reset claimerID to -1
        claimerID = -1;
    }
    public boolean numLeading0is(int difficulty,String hash){
    	String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0" 
		if(hash.substring( 0, difficulty).equals(target)){
			return true;
		}
    	return false;
    }
    @Override
    public void run() {
//      System.out.println("Running miner thread: " + this.getName());
        int nonce = Integer.MIN_VALUE;
        while (!consensusAchieved()) {
            while (!solutionClaimed && !numLeading0is(difficulty, Encryption.sha256("" + nonce + prevInfo))) {
                nonce++;
//                if (nonce == Integer.MAX_VALUE
//                        && !numLeading0is(difficulty, Encryption.sha256("" + nonce + prevInfo))) {
//                    prevInfo++;
//                    nonce = Integer.MIN_VALUE;
//                }
            }
            if (solutionClaimed) {
                // if someone else claims that a solution is found, verify that
                if (numLeading0is(difficulty, Encryption.sha256("" + candidate + prevInfo))) {
                    consensusList.set(index-1, true);
                } else {
                    // if this candidate fails the verification
                    resetConsensus();
                }
            } else if (numLeading0is(difficulty, Encryption.sha256("" + nonce + prevInfo))) {
                // if this miner finds a solution, report to the public, and wait for
                // verification
                solutionClaimed = true;
                consensusList.set(index-1, true);
                candidate = nonce;
                claimerID = index;
            }
        }
        final_nonce = String.valueOf(nonce);
        msg.add("Miner" + (this.index ) +  " has approved that Miner "
                + (claimerID + 1) + " came up with the correct solution: " + "\"" + final_nonce + "\" \n");
        System.out.println("Miner" + (this.index ) +  " has approved that Miner "
                + (claimerID + 1) + " came up with the correct solution: " + "\"" + final_nonce + "\" \n");

    }
}
