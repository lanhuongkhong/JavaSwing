/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DoAn.Dao;

import DoAn.Connect.KetNoi;
import DoAn.Model.SanPhamMD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SanPhamDao {
    
    public static ArrayList<SanPhamMD> getAll() throws Exception {
        ArrayList<SanPhamMD> lst = new ArrayList<>();
        String sql = "SELECT * FROM SanPham";
        
        try (Connection conn = KetNoi.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                SanPhamMD sanPham = new SanPhamMD();
                sanPham.setMaSanPham(rs.getString("MaSanPham"));
                sanPham.setTenSanPham(rs.getString("TenSanPham"));
                sanPham.setLoaiSanPham(rs.getString("LoaiSanPham"));
                sanPham.setGia(rs.getBigDecimal("Gia"));
                lst.add(sanPham);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Ideally, use a logger to log this exception
        }
        
        return lst;
    }

    public static SanPhamMD getById(String maSanPham) throws Exception {
        SanPhamMD sanPham = null;
        String sql = "SELECT * FROM SanPham WHERE MaSanPham = ?";
        
        try (Connection conn = KetNoi.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, maSanPham);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    sanPham = new SanPhamMD();
                    sanPham.setMaSanPham(rs.getString("MaSanPham"));
                    sanPham.setTenSanPham(rs.getString("TenSanPham"));
                    sanPham.setLoaiSanPham(rs.getString("LoaiSanPham"));
                    sanPham.setGia(rs.getBigDecimal("Gia"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Ideally, use a logger to log this exception
        }
        
        return sanPham;
    }

    public static boolean insert(SanPhamMD sanPham) throws Exception {
        String sql = "INSERT INTO SanPham (MaSanPham, TenSanPham, LoaiSanPham, Gia) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = KetNoi.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, sanPham.getMaSanPham());
            pstmt.setString(2, sanPham.getTenSanPham());
            pstmt.setString(3, sanPham.getLoaiSanPham());
            pstmt.setBigDecimal(4, sanPham.getGia());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace(); // Ideally, use a logger to log this exception
            return false;
        }
    }

    public static boolean update(SanPhamMD sanPham) throws Exception {
        String sql = "UPDATE SanPham SET TenSanPham = ?, LoaiSanPham = ?, Gia = ? WHERE MaSanPham = ?";
        
        try (Connection conn = KetNoi.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, sanPham.getTenSanPham());
            pstmt.setString(2, sanPham.getLoaiSanPham());
            pstmt.setBigDecimal(3, sanPham.getGia());
            pstmt.setString(4, sanPham.getMaSanPham());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace(); // Ideally, use a logger to log this exception
            return false;
        }
    }

    public static boolean delete(String maSanPham) throws Exception {
        String sql = "DELETE FROM SanPham WHERE MaSanPham = ?";
        
        try (Connection conn = KetNoi.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, maSanPham);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace(); // Ideally, use a logger to log this exception
            return false;
        }
    }
    public static ArrayList<SanPhamMD> searchByName(String tenSanPham) throws Exception {
        ArrayList<SanPhamMD> ketQua = new ArrayList<>();
        String sql = "SELECT * FROM SanPham WHERE TenSanPham LIKE ?";

        try (Connection conn = KetNoi.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + tenSanPham + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    SanPhamMD sanPham = new SanPhamMD();
                    sanPham.setMaSanPham(rs.getString("MaSanPham"));
                    sanPham.setTenSanPham(rs.getString("TenSanPham"));
                    sanPham.setLoaiSanPham(rs.getString("LoaiSanPham"));
                    sanPham.setGia(rs.getBigDecimal("Gia"));
                    ketQua.add(sanPham);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Ideally, use a logger to log this exception
        }

        return ketQua;
    }
    public static ArrayList<SanPhamMD> getProductsByType(String loaiSanPham) throws Exception {
    ArrayList<SanPhamMD> ketQua = new ArrayList<>();
    String sql = "SELECT * FROM SanPham WHERE LoaiSanPham = ?";

    try (Connection conn = KetNoi.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, loaiSanPham);
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                SanPhamMD sanPham = new SanPhamMD();
                sanPham.setMaSanPham(rs.getString("MaSanPham"));
                sanPham.setTenSanPham(rs.getString("TenSanPham"));
                sanPham.setLoaiSanPham(rs.getString("LoaiSanPham"));
                sanPham.setGia(rs.getBigDecimal("Gia"));
                ketQua.add(sanPham);
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace(); // Ideally, use a logger to log this exception
    }

    return ketQua;
}

     
}

