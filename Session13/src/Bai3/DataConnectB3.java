package Bai3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataConnectB3 {
    private final static String DRIVER = ("com.mysql.cj.jdbc.Driver");
    private final static String URL = ("jdbc:mysql://localhost:3306/Session13");
    private final static String USER = ("root");
    private final static String PASSWORD = ("cellfite90");

    private static Connection conn = null;
    public static Connection openConnect() throws SQLException {
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Kết nối thành công");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return conn;
    }
}
