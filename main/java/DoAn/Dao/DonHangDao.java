package DoAn.Dao;

import DoAn.Connect.KetNoi;
import DoAn.Model.DonHangMD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DonHangDao {
     public static ArrayList<DonHangMD> getAll() throws Exception {
        ArrayList<DonHangMD> lst = new ArrayList<>();
        String sql = "SELECT * FROM DonHang";
        
        try (Connection conn = KetNoi.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                DonHangMD donHang = new DonHangMD();
                donHang.setMaDonHang(rs.getString("MaDonHang"));
                donHang.setMaKhachHang(rs.getString("MaKhachHang"));
                donHang.setNgayDatHang(rs.getDate("NgayDatHang"));
                donHang.setTongTien(rs.getBigDecimal("TongTien"));
                donHang.setTrangThai(rs.getString("TrangThai"));
                lst.add(donHang);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Ideally, use a logger to log this exception
        }
        
        return lst;
    }

    public static DonHangMD getById(String maDonHang) throws Exception {
        DonHangMD donHang = null;
        String sql = "SELECT * FROM DonHang WHERE MaDonHang = ?";
        
        try (Connection conn = KetNoi.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, maDonHang);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    donHang = new DonHangMD();
                    donHang.setMaDonHang(rs.getString("MaDonHang"));
                    donHang.setMaKhachHang(rs.getString("MaKhachHang"));
                    donHang.setNgayDatHang(rs.getDate("NgayDatHang"));
                    donHang.setTongTien(rs.getBigDecimal("TongTien"));
                    donHang.setTrangThai(rs.getString("TrangThai"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Ideally, use a logger to log this exception
        }
        
        return donHang;
    }

  

}