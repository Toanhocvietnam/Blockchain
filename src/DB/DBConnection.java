package DB;
import java.sql.*;


public class DBConnection {
		public static Connection CreateConnect(){
		 Connection conn = null;
		 String database = "BlockChainDatabase";
		 String url = "jdbc:jtds:sqlserver://localhost:1433/"
		            + database + ";instance=SQLEXPRESS";
		 String username = "sa";
		 String password = "sa";
		 
		 // load driver
		 try {
				Class.forName("net.sourceforge.jtds.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		 // tao ket noi 
		 try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e){
			e.printStackTrace();
			System.out.println("Success");
		}
		 
		 return conn;
		 
	 }
		
	  
}
