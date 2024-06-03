/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DoAn.Dao;


import DoAn.Connect.KetNoi;
import DoAn.Model.KhachHangMD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
/**
 *
 * @author PC
 */
public class KhachHangDao {
    public static ArrayList<KhachHangMD> getALL() throws Exception {
        ArrayList<KhachHangMD> lst = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";
        
        try (Connection conn = KetNoi.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                KhachHangMD kh = new KhachHangMD();
                kh.setMaKhachHang(rs.getString("MaKhachHang"));
                kh.setHoTen(rs.getString("HoTen"));
                kh.setDienThoai(rs.getString("DienThoai"));
                kh.setDiaChi(rs.getString("DiaChi"));
                lst.add(kh);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Ideally, use a logger to log this exception
        }
        
        return lst;
    }

    public static KhachHangMD getById(String maKhachHang) throws Exception {
        KhachHangMD kh = null;
        String sql = "SELECT * FROM KhachHang WHERE MaKhachHang = ?";
        
        try (Connection conn = KetNoi.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, maKhachHang);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    kh = new KhachHangMD();
                    kh.setMaKhachHang(rs.getString("MaKhachHang"));
                    kh.setHoTen(rs.getString("HoTen"));
                    kh.setDienThoai(rs.getString("DienThoai"));
                    kh.setDiaChi(rs.getString("DiaChi"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Ideally, use a logger to log this exception
        }
        
        return kh;
    }

    public static boolean insert(KhachHangMD kh) throws Exception {
        String sql = "INSERT INTO KhachHang (MaKhachHang, HoTen, DienThoai, DiaChi) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = KetNoi.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, kh.getMaKhachHang());
            pstmt.setString(2, kh.getHoTen());
            pstmt.setString(3, kh.getDienThoai());
            pstmt.setString(4, kh.getDiaChi());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace(); // Ideally, use a logger to log this exception
            return false;
        }
    }

    public static boolean update(KhachHangMD kh) throws Exception {
        String sql = "UPDATE KhachHang SET HoTen = ?, DienThoai = ?, DiaChi = ? WHERE MaKhachHang = ?";
        
        try (Connection conn = KetNoi.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, kh.getHoTen());
            pstmt.setString(2, kh.getDienThoai());
            pstmt.setString(3, kh.getDiaChi());
            pstmt.setString(4, kh.getMaKhachHang());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace(); // Ideally, use a logger to log this exception
            return false;
        }
    }

    public static boolean delete(String maKhachHang) throws Exception {
        String sql = "DELETE FROM KhachHang WHERE MaKhachHang = ?";
        
        try (Connection conn = KetNoi.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, maKhachHang);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace(); // Ideally, use a logger to log this exception
            return false;
        }
    }
 

}

