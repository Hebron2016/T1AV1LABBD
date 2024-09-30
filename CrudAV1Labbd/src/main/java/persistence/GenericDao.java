package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class GenericDao {

		private Connection c;
		
		public Connection getConnection()throws ClassNotFoundException, SQLException{
			String hostName = "localhost";
			String dbName = "master";
			String user = "viewMyDatabase";
			String senha = "123456";
			Class.forName("net.sourceforge.jtds.jdbc");
			c = DriverManager.getConnection(String.format(
					"jdbc:jtds:sqlserver://%s:1433;databaseName=%s;user=%s;password=%s;encrypt=false;", hostName, dbName, user, senha));
			return c;
		}

}
