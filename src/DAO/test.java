package DAO;

import java.sql.Connection;
import java.util.ArrayList;

import DB.DBConnection;
import BEAN.*;
public class test {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection conn = DBConnection.CreateConnect();
		ArrayList<Block> blockChain =BlockDAO.getBlockChain();
		blockChain.get(0).getIndex();
		System.out.println(blockChain.size());
	}
}
