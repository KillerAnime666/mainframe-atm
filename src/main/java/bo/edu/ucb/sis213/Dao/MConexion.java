package bo.edu.ucb.sis213.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MConexion {
	
	private static final String USER = "root";
	private static final String PASSWORD = "";
	private static final String HOST = "127.0.0.1";
	private static final String DATABASE = "atm";
	private static final int PORT = 3306;
	
	public static Connection getConnection() throws SQLException {
        String jdbcUrl = String.format("jdbc:mysql://%s:%d/%s", HOST, PORT, DATABASE);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL Driver not found.", e);
        }
        return DriverManager.getConnection(jdbcUrl, USER, PASSWORD);
    }
}
