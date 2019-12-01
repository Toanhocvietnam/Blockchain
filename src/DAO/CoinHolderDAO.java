package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import BEAN.*;
import DB.DBConnection;
public class CoinHolderDAO {
	public static ArrayList<CoinHolder> getAllCoinHolder(){
		Connection conn = DBConnection.CreateConnect();
		ArrayList<CoinHolder> chList = new ArrayList<CoinHolder>();
		String sql="SELECT * FROM NODE";
		try 
		{
			PreparedStatement ptmt = conn.prepareStatement(sql);
			
			ResultSet rs = ptmt.executeQuery();
			
			while(rs.next()) {
			 int id= rs.getInt("id");
			 String username = rs.getString("username");
			 float balance = rs.getFloat("balance");
			 CoinHolder coinHolder = new CoinHolder(id,username,balance);
			 chList.add(coinHolder);
			}
				
		} 
		catch (SQLException e) 
		{
			
			e.printStackTrace();
		}
		return chList;
	}
	
	public static void updateCoinHolder(int id, double balance) {
		Connection conn = DBConnection.CreateConnect();
		String sql="UPDATE NODE "
				+ "SET balance = "+ balance
				+ " WHERE id = " +id;
		try 
		{
			PreparedStatement ptmt = conn.prepareStatement(sql);
			ptmt.executeUpdate();
			
			
				
		} 
		catch (SQLException e) 
		{
			
			e.printStackTrace();
		}
	}
}
