package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import BEAN.*;
import DB.DBConnection;
public class BlockDAO {
	public static ArrayList<Block> getBlockChain(){
		Connection conn = DBConnection.CreateConnect();
		ArrayList<Block> blockChain = new ArrayList<Block>();
		String sql = "SELECT * FROM BLOCK";
		try 
		{
			PreparedStatement ptmt = conn.prepareStatement(sql);
			
			ResultSet rs = ptmt.executeQuery();
			
			while(rs.next()) {
			 int id= rs.getInt("id");
			 String note = rs.getString("note");
			 String prev_hash = rs.getString("prev_hash");
			 String cur_hash = rs.getString("cur_hash");
			 String nonce = rs.getString("nounce");
			 Block block = new Block(id, note,prev_hash,cur_hash,nonce);
			 blockChain.add(block);
			 
			}
			conn.close();
				
		} 
		catch (SQLException e) 
		{
			
			e.printStackTrace();
		}
		
		return blockChain;
	}
	
	public static ArrayList<Block> getBlockByPage(ArrayList<Block> arr, int start, int end){
		ArrayList<Block> list = new ArrayList<Block>();
		for(int i = start; i<=end;i++) {
			list.add(arr.get(i));
		}
		return list;
	}
	public static Block getLastBlock() {
		Connection conn = DBConnection.CreateConnect();
		String sql="SELECT TOP 1 * FROM BLOCK ORDER BY ID DESC";
		Block block=new Block();
		try 
		{
			PreparedStatement ptmt = conn.prepareStatement(sql);
			
			ResultSet rs = ptmt.executeQuery();
			
			if(rs.next()) {
			 int id= rs.getInt("id");
			 String note = rs.getString("note");
			 String prev_hash = rs.getString("prev_hash");
			 String cur_hash = rs.getString("cur_hash");
			 String nonce = rs.getString("nounce");
			 block = new Block(id, note,prev_hash,cur_hash,nonce);
			 return block;
			}
			conn.close();
				
		} 
		catch (SQLException e) 
		{
			
			e.printStackTrace();
		}
		return block;
		
		
	}
	
	public static void insertBlock(Block block) {
        try {

            //Conect
            Connection conn = DBConnection.CreateConnect();
            String sql = "INSERT INTO Block(id,timstamp,note, prev_hash, cur_hash,difficulty, nounce) VALUES(" + block.getIndex() + "," + block.getTimestamp() + ",'" + block.getNote() + "','" + block.getPrev_hash() + "','" 
            + block.getHash() + "'," + block.getDifficulty() + ",'" + block.getNonce() +"')" ;

            PreparedStatement ptmt = conn.prepareStatement(sql);

            ptmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("tb");
        }
    }
	
}
