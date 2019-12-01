package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import BEAN.*;
import DB.DBConnection;
public class TransactionDAO {
	public static void insertTransaction(Transaction transaction){
        try {

            //Conect
            Connection conn = DBConnection.CreateConnect();
            int isVerified = 0;
            if(transaction.isVerified() == true) {
            	isVerified =1;
            }
            String sql = "INSERT INTO Transac(id, blockID, senderID, recepitentID, amount, verified) VALUES("+transaction.getIdTran()+","+transaction.getBlockId()+","+transaction.getSenderID()+", "+
                    transaction.getRecipientID()+","+ transaction.getAmount()+", "+isVerified+")";
            PreparedStatement ptmt = conn.prepareStatement(sql);

            ptmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("tb");
        }
    }
}
