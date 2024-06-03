/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DoAn.Connect;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
/**
 *
 * @author PC
 */
public class KetNoi {
    private static final String userName = "sa";
    private static final String password = "thailan123";
    private static final String url = "jdbc:sqlserver://LAPTOP-NDV3V8U4\\SQLEXPRESS:1433;encrypt=true;trustServerCertificate=true;databaseName=DoANJW";
    
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Không thể kết nối đến cơ sở dữ liệu!");
            e.printStackTrace();
        }
        return conn;
    }

    public static void closeConnection(Connection conn, Statement stmt, ResultSet rs) {
    try {
        if (rs != null) rs.close();
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
    }
    
}
