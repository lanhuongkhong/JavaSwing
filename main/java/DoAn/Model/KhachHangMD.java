    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DoAn.Model;

/**
 *
 * @author PC
 */
public class KhachHangMD {
    private String MaKhachHang;
    private String HoTen;
    private String DienThoai;
    private String DiaChi;

    // Constructor
    public KhachHangMD(String maKhachHang, String hoTen, String dienThoai, String diaChi) {
        this.MaKhachHang = maKhachHang;
        this.HoTen = hoTen;
        this.DienThoai = dienThoai;
        this.DiaChi = diaChi;
    }
    public KhachHangMD(){
        
    }  

    // Getters
    public String getMaKhachHang() {
        return MaKhachHang;
    }

    public String getHoTen() {
        return HoTen;
    }

    public String getDienThoai() {
        return DienThoai;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    // Setters
    public void setMaKhachHang(String maKhachHang) {
        this.MaKhachHang = maKhachHang;
    }

    public void setHoTen(String hoTen) {
        this.HoTen = hoTen;
    }

    public void setDienThoai(String dienThoai) {
        this.DienThoai = dienThoai;
    }

    public void setDiaChi(String diaChi) {
        this.DiaChi = diaChi;
    }
}