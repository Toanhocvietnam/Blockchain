package Controller;
import BEAN.*;
import DAO.*;
public class createGenesisBlock {

	public static void main(String[] args) {
		BlockChain blockchain = new BlockChain();
		blockchain.loadCoinHolder();
		Block genesisBlock = blockchain.createGenesisBlock();
		BlockDAO.insertBlock(genesisBlock);
		for(int i=0; i< genesisBlock.getTransactions().length;i++) {
			TransactionDAO.insertTransaction(genesisBlock.getTransactions()[i]);
		}
	
		
	}	

}
