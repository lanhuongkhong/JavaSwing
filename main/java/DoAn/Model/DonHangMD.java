/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DoAn.Model;

import java.math.BigDecimal;
import java.util.Date;

public class DonHangMD {
    private String maDonHang;
    private String maKhachHang;
    private Date ngayDatHang; 
    private BigDecimal tongTien;
    private String trangThai;

    // Constructors
    public DonHangMD(String maDonHang, String maKhachHang, Date ngayDatHang, BigDecimal tongTien, String trangThai) {
        this.maDonHang = maDonHang;
        this.maKhachHang = maKhachHang;
        this.ngayDatHang = ngayDatHang; // Corrected parameter name
        this.tongTien = tongTien;
        this.trangThai = trangThai;
    }

    public DonHangMD() {
    }

    // Getters
    public String getMaDonHang() {
        return maDonHang;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public Date getNgayDatHang() { // Corrected getter method name
        return ngayDatHang;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    // Setters
    public void setMaDonHang(String maDonHang) {
        this.maDonHang = maDonHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public void setNgayDatHang(Date ngayDatHang) { // Corrected setter method name
        this.ngayDatHang = ngayDatHang;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
