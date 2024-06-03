/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DoAn.Model;

import java.math.BigDecimal;

/**
 *
 * @author PC
 */
public class ChiTietDonHangMD {
    private String MaChiTietDonHang;
    private String MaDonHang;
    private String MaSanPham;
    private int SoLuong;
    private BigDecimal DonGia;

    // Constructor mặc định
    public ChiTietDonHangMD() {
    }

    // Constructor có tham số
    public ChiTietDonHangMD(String MaChiTietDonHang, String MaDonHang, String MaSanPham, int SoLuong, BigDecimal DonGia) {
        this.MaChiTietDonHang = MaChiTietDonHang;
        this.MaDonHang = MaDonHang;
        this.MaSanPham = MaSanPham;
        this.SoLuong = SoLuong;
        this.DonGia = DonGia;
    }

    // Getter và Setter cho MaChiTietDonHang
    public String getMaChiTietDonHang() {
        return MaChiTietDonHang;
    }

    public void setMaChiTietDonHang(String MaChiTietDonHang) {
        this.MaChiTietDonHang = MaChiTietDonHang;
    }

    // Getter và Setter cho MaDonHang
    public String getMaDonHang() {
        return MaDonHang;
    }

    public void setMaDonHang(String MaDonHang) {
        this.MaDonHang = MaDonHang;
    }

    // Getter và Setter cho MaSanPham
    public String getMaSanPham() {
        return MaSanPham;
    }

    public void setMaSanPham(String MaSanPham) {
        this.MaSanPham = MaSanPham;
    }

    // Getter và Setter cho SoLuong
    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int SoLuong) {
        this.SoLuong = SoLuong;
    }

    // Getter và Setter cho DonGia
    public BigDecimal getDonGia() {
        return DonGia;
    }

    public void setDonGia(BigDecimal DonGia) {
        this.DonGia = DonGia;
    }   
}
