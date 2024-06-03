package DoAn.Dao;

import DoAn.Connect.KetNoi;
import DoAn.Model.HoaDonMD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class HoaDonDao {

    // Method to get all HoaDon records
    public ArrayList<HoaDonMD> getAll() throws SQLException {
        ArrayList<HoaDonMD> lst = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon";

        try (Connection conn = KetNoi.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                HoaDonMD hoaDon = new HoaDonMD(
                        rs.getString("maHoaDon"),
                        rs.getInt("soLuong"),
                        rs.getBigDecimal("gia"),
                        rs.getDate("ngayTao")
                );
                lst.add(hoaDon);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Ideally, use a logger to log this exception
        }

        return lst;
    }
    public String getLatestInvoiceId() throws Exception {
    String latestInvoiceId = null;
    String sql = "SELECT TOP 1 maHoaDon FROM HoaDon ORDER BY maHoaDon DESC"; // Select the top 1 invoice ID ordered by descending order
    
    try (Connection conn = KetNoi.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {
        
        if (rs.next()) {
            latestInvoiceId = rs.getString("maHoaDon");
        }
    } catch (SQLException ex) {
        ex.printStackTrace(); // Handle exceptions appropriately
    }
    
    return latestInvoiceId;
}


    // Method to get a HoaDon record by ID
    public HoaDonMD getById(String maHoaDon) throws SQLException {
        HoaDonMD hoaDon = null;
        String sql = "SELECT * FROM HoaDon WHERE maHoaDon = ?";

        try (Connection conn = KetNoi.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maHoaDon);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    hoaDon = new HoaDonMD(
                            rs.getString("maHoaDon"),
                            rs.getInt("soLuong"),
                            rs.getBigDecimal("gia"),
                            rs.getDate("ngayTao")
                    );
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Ideally, use a logger to log this exception
        }

        return hoaDon;
    }

    // Method to insert a new HoaDon record
    public long insertAndReturnId(HoaDonMD hoaDon) throws SQLException {
    long maHoaDon = -1;
    String sql = "INSERT INTO HoaDon (maHoaDon, soLuong, gia, ngayTao) VALUES (?, ?, ?, ?)";

    try (Connection conn = KetNoi.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        pstmt.setString(1, hoaDon.getMaHoaDon());
        pstmt.setInt(2, hoaDon.getSoLuong());
        pstmt.setBigDecimal(3, hoaDon.getGia());
        pstmt.setDate(4, new java.sql.Date(hoaDon.getNgayTao().getTime()));

        int rowsInserted = pstmt.executeUpdate();

        if (rowsInserted > 0) {
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    maHoaDon = rs.getLong(1);
                }
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace(); // Ideally, use a logger to log this exception
    }

    return maHoaDon;
}


    // Method to update an existing HoaDon record
    public boolean update(HoaDonMD hoaDon) throws SQLException {
        String sql = "UPDATE HoaDon SET soLuong = ?, gia = ?, ngayTao = ? WHERE maHoaDon = ?";

        try (Connection conn = KetNoi.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, hoaDon.getSoLuong());
            pstmt.setBigDecimal(2, hoaDon.getGia());
            pstmt.setDate(3, new java.sql.Date(hoaDon.getNgayTao().getTime()));
            pstmt.setString(4, hoaDon.getMaHoaDon());

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException ex) {
            ex.printStackTrace(); // Ideally, use a logger to log this exception
            return false;
        }
    }

    // Method to delete a HoaDon record
    public boolean delete(String maHoaDon) throws SQLException {
        String sql = "DELETE FROM HoaDon WHERE maHoaDon = ?";

        try (Connection conn = KetNoi.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maHoaDon);

            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException ex) {
            ex.printStackTrace(); // Ideally, use a logger to log this exception
            return false;
        }
    }
        public int getTongSoLuongTrongKhoangThoiGian(String ngayBatDau, String ngayKetThuc) throws SQLException {
        int tongSoLuong = 0;
        String sql = "SELECT SUM(soLuong) AS tongSoLuong FROM HoaDon WHERE ngayTao BETWEEN ? AND ?";

        try (Connection conn = KetNoi.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ngayBatDau);
            pstmt.setString(2, ngayKetThuc);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    tongSoLuong = rs.getInt("tongSoLuong");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Xử lý các ngoại lệ theo cách phù hợp
        }

        return tongSoLuong;
    }

    // Phương thức tính tổng giá tiền trong khoảng thời gian đã cho
    public float getTongGiaTienTrongKhoangThoiGian(String ngayBatDau, String ngayKetThuc) throws SQLException {
        float tongGiaTien = 0;
        String sql = "SELECT SUM(gia) AS tongGiaTien FROM HoaDon WHERE ngayTao BETWEEN ? AND ?";

        try (Connection conn = KetNoi.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ngayBatDau);
            pstmt.setString(2, ngayKetThuc);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    tongGiaTien = rs.getFloat("tongGiaTien");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Xử lý các ngoại lệ theo cách phù hợp
        }

        return tongGiaTien;
    }


  
}
