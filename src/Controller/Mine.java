package Controller;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import DAO.*;
import DB.DBConnection;
import BEAN.*;
/**
 * Servlet implementation class Mine
 */
@WebServlet("/Mine")
public class Mine extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Mine() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BlockChain blockchain = new BlockChain();
		blockchain.loadCoinHolder();
		blockchain.loadMiners();
		blockchain.loadBlockChain();
		
		blockchain.simulateTransactions();
//		for(int i=0; i<blockchain.transactions.size(); i ++) {
//			System.out.println(blockchain.transactions.get(i).getIdTran());
//		}
		Block newBlock = blockchain.mine();
		blockchain.blockchain.add(newBlock);
		BlockDAO.insertBlock(newBlock);
		blockchain.insertTxions(newBlock);
		System.out.println(newBlock.getMsg());
		System.out.println();
        System.out.println("Block " + newBlock.getIndex()+ " is mine by Miner " + (Miner.claimerID+1));
        System.out.println();
        System.out.println("Block Information");
        System.out.println(newBlock.toString());
		
        blockchain.updateCoinHolders();
        blockchain.updateBalanceDB();
        request.setAttribute("block", newBlock);
			
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
