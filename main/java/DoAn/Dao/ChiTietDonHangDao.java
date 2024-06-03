/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DoAn.Dao;

import DoAn.Connect.KetNoi;
import DoAn.Model.ChiTietDonHangMD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChiTietDonHangDao {
    
    public static ArrayList<ChiTietDonHangMD> getAll() throws Exception {
        ArrayList<ChiTietDonHangMD> lst = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietDonHang";
        
        try (Connection conn = KetNoi.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                ChiTietDonHangMD chiTietDonHang = new ChiTietDonHangMD();
                chiTietDonHang.setMaChiTietDonHang(rs.getString("MaChiTietDonHang"));
                chiTietDonHang.setMaDonHang(rs.getString("MaDonHang"));
                chiTietDonHang.setMaSanPham(rs.getString("MaSanPham"));
                chiTietDonHang.setSoLuong(rs.getInt("SoLuong"));
                chiTietDonHang.setDonGia(rs.getBigDecimal("DonGia"));
                lst.add(chiTietDonHang);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Ideally, use a logger to log this exception
        }
        
        return lst;
    }

    public static ChiTietDonHangMD getById(String maChiTietDonHang) throws Exception {
        ChiTietDonHangMD chiTietDonHang = null;
        String sql = "SELECT * FROM ChiTietDonHang WHERE MaChiTietDonHang = ?";
        
        try (Connection conn = KetNoi.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, maChiTietDonHang);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    chiTietDonHang = new ChiTietDonHangMD();
                    chiTietDonHang.setMaChiTietDonHang(rs.getString("MaChiTietDonHang"));
                    chiTietDonHang.setMaDonHang(rs.getString("MaDonHang"));
                    chiTietDonHang.setMaSanPham(rs.getString("MaSanPham"));
                    chiTietDonHang.setSoLuong(rs.getInt("SoLuong"));
                    chiTietDonHang.setDonGia(rs.getBigDecimal("DonGia"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Ideally, use a logger to log this exception
        }
        
        return chiTietDonHang;
    }

    public static boolean insert(ChiTietDonHangMD chiTietDonHang) throws Exception {
        String sql = "INSERT INTO ChiTietDonHang (MaChiTietDonHang, MaDonHang, MaSanPham, SoLuong, DonGia) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = KetNoi.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, chiTietDonHang.getMaChiTietDonHang());
            pstmt.setString(2, chiTietDonHang.getMaDonHang());
            pstmt.setString(3, chiTietDonHang.getMaSanPham());
            pstmt.setInt(4, chiTietDonHang.getSoLuong());
            pstmt.setBigDecimal(5, chiTietDonHang.getDonGia());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace(); // Ideally, use a logger to log this exception
            return false;
        }
    }

    public static boolean update(ChiTietDonHangMD chiTietDonHang) throws Exception {
        String sql = "UPDATE ChiTietDonHang SET MaDonHang = ?, MaSanPham = ?, SoLuong = ?, DonGia = ? WHERE MaChiTietDonHang = ?";
        
        try (Connection conn = KetNoi.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, chiTietDonHang.getMaDonHang());
            pstmt.setString(2, chiTietDonHang.getMaSanPham());
            pstmt.setInt(3, chiTietDonHang.getSoLuong());
            pstmt.setBigDecimal(4, chiTietDonHang.getDonGia());
            pstmt.setString(5, chiTietDonHang.getMaChiTietDonHang());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace(); // Ideally, use a logger to log this exception
            return false;
        }
    }

    public static boolean delete(String maChiTietDonHang) throws Exception {
    String sql = "DELETE FROM ChiTietDonHang WHERE MaChiTietDonHang = ?";
    
    try (Connection conn = KetNoi.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
         
        pstmt.setString(1, maChiTietDonHang);
        return pstmt.executeUpdate() > 0;
    } catch (SQLException ex) {
        ex.printStackTrace(); // Ideally, use a logger to log this exception
        return false;
    }
}
}