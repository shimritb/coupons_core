package db_services.connection_pool;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ConnectionPool {
	private static ConnectionPool instance = null;
//	private final String url = "jdbc:sqlserver://localhost:1433;";
//	private final String dbName = "coupons;";
//	private final String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
//	private final String userName = "couponsadmin;";
//	private final String password = "12344321;";


	private static Set<Connection> availableConnections = new HashSet<Connection>();
	private static int maxAvaliableConn = 5; //the max number of connection will be 5

	/**
	 * private Constructor
	 */
	private ConnectionPool() {
	}
	/**
	 * This method creates a new connection to the pool
	 * @return Connection
	 */
	public Connection getConnection() {
		Connection returnCon = null;

		synchronized (availableConnections) {
			if (availableConnections.size() < maxAvaliableConn) {
				try {
					Class.forName("com.mysql.jdbc.Driver").newInstance();

					returnCon = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/coupons","root", "shimrit");

					availableConnections.add(returnCon);

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else
				try {
					availableConnections.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}

		System.out.println("Active Connections per moment: " + availableConnections.size());
		return returnCon;
	}
	/**
	 * This method closes the active connection
	 * @param con
	 */
	public void returnConnection(Connection con) {
		synchronized (availableConnections) {
			// access via Iterator
			Iterator<Connection> iterator = availableConnections.iterator();
			
			while(iterator.hasNext()) {
				Connection tmp = iterator.next();
				if(tmp.hashCode() == con.hashCode()){
					iterator.remove();	// removes the connection from the collection
					
					try {	// closes the connection if it matches the hashcode
						tmp.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					availableConnections.notify();
				}
			}
			System.out.println("Active Connections per moment: " + availableConnections.size());
		}
	}
	/**
	 * This methos closes all the connections in the pool
	 */
	public void closeAllConnections(){
		for(Connection con:availableConnections){
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}	
		System.out.println("All connections are closed");
	}
	
	/**
	 * This method creates one new instance of a connectionPool , there will be no more the one instance of that object
	 * @return ConnectionPool
	 */
	public static synchronized ConnectionPool getInstance() {
		if (instance == null) {
			instance = new ConnectionPool();
		}

		return instance;
	}
}
