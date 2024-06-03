
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DoAn.Dao;

import DoAn.Connect.KetNoi;
import DoAn.Model.NhanVienMD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author PC
 */
public class NhanVienDao {
    public static ArrayList<NhanVienMD> getALL() throws Exception {
        ArrayList<NhanVienMD> lst = new ArrayList<>();
        Connection conn = KetNoi.getConnection();
          try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM NhanVien";
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                  NhanVienMD nv = new NhanVienMD();
                  nv.setMaNhanVien(rs.getString(1));
                  nv.setHoTen(rs.getString(2));
                  nv.setDienThoai(rs.getString(3));
                  nv.setViTri(rs.getString(4));
                lst.add(nv);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            conn.close();
        }
        return lst;
    }
     public static boolean insert(NhanVienMD nv) throws Exception {
        Connection conn = KetNoi.getConnection();
        try {
            String sql = "INSERT INTO NhanVien (MaNhanVien, HoTen, DienThoai, ViTri) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nv.getMaNhanVien());
            pstmt.setString(2, nv.getHoTen());
            pstmt.setString(3, nv.getDienThoai());
            pstmt.setString(4, nv.getViTri());
            int rowsInserted = pstmt.executeUpdate();
            pstmt.close();
            return rowsInserted > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            conn.close();
        }
    }

    public static boolean update(NhanVienMD nv) throws Exception {
        Connection conn = KetNoi.getConnection();
        try {
            String sql = "UPDATE NhanVien SET HoTen = ?, DienThoai = ?, ViTri = ? WHERE MaNhanVien = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nv.getHoTen());
            pstmt.setString(2, nv.getDienThoai());
            pstmt.setString(3, nv.getViTri());
            pstmt.setString(4, nv.getMaNhanVien());
            int rowsUpdated = pstmt.executeUpdate();
            pstmt.close();
            return rowsUpdated > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            conn.close();
        }
    }

    public static boolean delete(String maNhanVien) throws Exception {
        Connection conn = KetNoi.getConnection();
        try {
            String sql = "DELETE FROM NhanVien WHERE MaNhanVien = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maNhanVien);
            int rowsDeleted = pstmt.executeUpdate();
            pstmt.close();
            return rowsDeleted > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            conn.close();
        }
    }
    public static ArrayList<NhanVienMD> searchByName(String hoTen) throws SQLException {
        ArrayList<NhanVienMD> ketQua = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = KetNoi.getConnection();
            String sql = "SELECT * FROM NhanVien WHERE HoTen LIKE ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + hoTen + "%");

            rs = pstmt.executeQuery();

            while (rs.next()) {
                NhanVienMD nv = new NhanVienMD();
                nv.setMaNhanVien(rs.getString("MaNhanVien"));
                nv.setHoTen(rs.getString("HoTen"));
                nv.setDienThoai(rs.getString("DienThoai"));
                nv.setViTri(rs.getString("ViTri"));
                ketQua.add(nv);
            }
        } finally {
            // Đảm bảo đóng tài nguyên sau khi sử dụng
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return ketQua;
    }
    public static ArrayList<NhanVienMD> getEmployeesByPosition(String viTri) throws Exception {
    ArrayList<NhanVienMD> ketQua = new ArrayList<>();
    String sql = "SELECT * FROM NhanVien WHERE ViTri = ?";

    try (Connection conn = KetNoi.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, viTri);
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                NhanVienMD nhanVien = new NhanVienMD();
                nhanVien.setMaNhanVien(rs.getString("MaNhanVien"));
                nhanVien.setHoTen(rs.getString("HoTen"));
                nhanVien.setDienThoai(rs.getString("DienThoai"));
                nhanVien.setViTri(rs.getString("ViTri"));
                ketQua.add(nhanVien);
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace(); // Lý tưởng nhất là sử dụng một logger để ghi lại ngoại lệ này
    }

    return ketQua;
}

public static ArrayList<NhanVienMD> getAllEmployees() throws Exception {
    ArrayList<NhanVienMD> ketQua = new ArrayList<>();
    String sql = "SELECT * FROM NhanVien";

    try (Connection conn = KetNoi.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
            NhanVienMD nhanVien = new NhanVienMD();
            nhanVien.setMaNhanVien(rs.getString("MaNhanVien"));
            nhanVien.setHoTen(rs.getString("HoTen"));
            nhanVien.setDienThoai(rs.getString("DienThoai"));
            nhanVien.setViTri(rs.getString("ViTri"));
            ketQua.add(nhanVien);
        }
    } catch (SQLException ex) {
        ex.printStackTrace(); // Lý tưởng nhất là sử dụng một logger để ghi lại ngoại lệ này
    }

    return ketQua;
}

}
